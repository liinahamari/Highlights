package dev.liinahamari.suggestions.sample.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.suggestions.api.model.RemoteMovie
import dev.liinahamari.suggestions.sample.R
import dev.liinahamari.suggestions.sample.databinding.FragmentMainBinding
import dev.liinahamari.suggestions.sample.databinding.FragmentMoviePreviewBinding
import java.time.Year

class MainFragment : Fragment(R.layout.fragment_main) {
    private val fromCategory: Category by lazy { Category.GOOD }
    private val ui by viewBinding(FragmentMainBinding::bind)
    private val suggestionsAdapter: ArrayAdapter<RowItem> by lazy { PicturedArrayAdapter(requireContext()) }

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        setupViewModelSubscriptions()
    }

    private fun setupViews() = ui.enterMovieNameEt.doOnTextChanged { text, _, _, _ ->
        viewModel.searchMovie(text.toString())
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
                        addAll(it.movies.map {
                            RowItem(
                                posterUrl = it.posterPath,
                                title = it.title ?: "",
                                year = Year.of(it.releaseDate?.substring(0, 4)?.toInt() ?: 0)
                            )
                        })
                        filter.filter(null)
                    }
                    ui.enterMovieNameEt.setOnItemClickListener { _, _, position, _ ->
                        RemoteMoviePreviewFragment.newInstance(it.movies[position], fromCategory)
                            .show(parentFragmentManager, "abc")
                    }
                }
            }
        }
    }
}

class RemoteMoviePreviewFragment : DialogFragment() {
    private val ui by viewBinding(FragmentMoviePreviewBinding::bind)
    private val movieUi by lazy { requireArguments().getParcelableOf<RemoteMovie>(ARG_MOVIE) }
    private val category by lazy { requireArguments().getParcelableOf<Category>(ARG_MOVIE) }
    private val moviePreviewViewModel: MoviePreviewFragmentViewModel by viewModels()

    companion object {
        private const val ARG_MOVIE = "arg_mov"
        private const val ARG_CATEGORY = "arg_category"
        fun newInstance(movie: RemoteMovie, category: Category) =
            RemoteMoviePreviewFragment().apply { arguments = bundleOf(ARG_MOVIE to movie, ARG_CATEGORY to category) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(ui) {
        renderMovieInformation()
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() = with(ui) {
        importBtn.setOnClickListener { moviePreviewViewModel.saveMovie(movieUi, category) } // todo make state "pressed"
        cancelBtn.setOnClickListener { dismiss() }
    }

    private fun renderMovieInformation() = with(ui) {
        Glide.with(requireContext()).load("https://image.tmdb.org/t/p/w500${movieUi.posterPath}").timeout(20_000)
            .into(ui.posterIv)
        titleTv.text = movieUi.title
        yearTv.text = movieUi.releaseDate
        genresTv.text = movieUi.genres.toString()
        countriesTv.text = movieUi.originCountry.toString()
    }
}

class MoviePreviewFragmentViewModel : ViewModel() {
    fun saveMovie(movie: RemoteMovie, category: Category): Nothing = TODO("Not yet implemented")
}
