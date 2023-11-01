package dev.liinahamari.suggestions.sample.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.suggestions.api.model.RemoteMovie
import dev.liinahamari.suggestions.sample.R
import dev.liinahamari.suggestions.sample.databinding.FragmentMainBinding
import dev.liinahamari.suggestions.sample.databinding.FragmentMoviePreviewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable


class MainFragment : Fragment(R.layout.fragment_main) {
    private val ui by viewBinding(FragmentMainBinding::bind)
    private val suggestionsAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line, emptyList()
        ).apply { setNotifyOnChange(true) }
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ui.enterMovieNameEt.doOnTextChanged { text, _, _, _ ->
            viewModel.searchForMovie(text.toString())
        }
        setupViewModelSubscriptions()
    }

    private fun setupViewModelSubscriptions() {
        ui.enterMovieNameEt.setAdapter(suggestionsAdapter)
        viewModel.searchMoviesEvent.observe(viewLifecycleOwner) {
            when (it) {
                is GetRemoteMovies.Error.CommonError -> toast("Suggestions API failed")
                is GetRemoteMovies.Error.NoInternetError -> toast("Check the Internet connection")

                is GetRemoteMovies.Success -> {
                    with(suggestionsAdapter) {
                        clear()
                        addAll(it.movies.map { it.title })
                        filter.filter(null)
                    }
                    ui.enterMovieNameEt
                        .apply {
                            setOnItemClickListener { _, _, position, _ ->
                                RemoteMoviePreviewFragment.newInstance(it.movies[position])
                                    .show(parentFragmentManager, "abc")
                            }
                        }
                }
            }
        }
    }
}

class RemoteMoviePreviewFragment : DialogFragment(R.layout.fragment_movie_preview) {
    private val ui by viewBinding(FragmentMoviePreviewBinding::bind)
    private val movieUi by lazy { requireArguments().getParcelableOf<RemoteMovie>(ARG_MOVIE) }
    private val moviePreviewFragmentViewModel: MoviePreviewFragmentViewModel by viewModels()

    companion object {
        private const val ARG_MOVIE = "arg_mov"
        fun newInstance(movie: RemoteMovie) =
            RemoteMoviePreviewFragment().apply { arguments = bundleOf(ARG_MOVIE to movie) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
        .setPositiveButton("Import") { _, _ -> moviePreviewFragmentViewModel.saveMovie(movieUi) }
        .setNegativeButton(android.R.string.cancel, null)
        .create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(ui) {
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500${movieUi.posterPath}")
            .timeout(20_000)
            .into(ui.posterIv)
        titleTv.text = movieUi.title
        yearTv.text = movieUi.releaseDate
        genresTv.text = movieUi.genres.toString()
        countriesTv.text = movieUi.originCountry.toString()
    }
}

class MoviePreviewFragmentViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    override fun onCleared() = disposable.clear()

    fun saveMovie(movie: RemoteMovie) {}
}
