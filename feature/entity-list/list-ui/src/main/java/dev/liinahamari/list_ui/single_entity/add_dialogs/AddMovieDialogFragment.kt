package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.app.Dialog
import android.app.SearchManager.QUERY
import android.content.Context
import android.content.DialogInterface.BUTTON_NEUTRAL
import android.content.Intent
import android.content.Intent.ACTION_WEB_SEARCH
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.api.domain.entities.MovieGenre
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.MainActivity
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentAddMovieBinding
import dev.liinahamari.list_ui.viewmodels.SaveEntryViewModel
import dev.liinahamari.list_ui.viewmodels.SaveEvent
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY
import dev.liinahamari.suggestions_ui.movie.SearchMovieAutoCompleteTextView
import javax.inject.Inject

class AddMovieDialogFragment : DialogFragment(R.layout.fragment_add_movie) {
    private var _ui: FragmentAddMovieBinding? = null
    private val ui: FragmentAddMovieBinding by lazy { _ui!! }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }

    private var movie = Movie.default()

    companion object {
        fun newInstance(category: Category): AddMovieDialogFragment = AddMovieDialogFragment().apply {
            arguments = bundleOf(ARG_CATEGORY to category)
        }
    }

    override fun onAttach(context: Context) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
        .setView((FragmentAddMovieBinding.inflate(layoutInflater)).also { _ui = it }.root)
        .setNeutralButton(R.string.internet_search) { _, _ -> }
        .setPositiveButton(R.string.save) { _, _ -> saveEntryViewModel.saveMovie(movie) }
        .create()
        .apply {
            setOnShowListener {
                (dialog as AlertDialog).getButton(BUTTON_NEUTRAL)
                    .setOnClickListener {
                        startActivity(
                            Intent(ACTION_WEB_SEARCH)
                                .putExtra(QUERY, ui.titleEt.text.toString() + " film (${ui.yearEt.text})")
                        )
                    }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupViewModelSubscriptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    private fun setupViewModelSubscriptions() = saveEntryViewModel.saveEvent.observe(this) {
        when (it) {
            is SaveEvent.Failure -> toast("Failed to save movie")
            is SaveEvent.Success -> requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun setupUi() {
        var selectedCountries = listOf<String>()
        var selectedGenres = listOf<MovieGenre>()

        ui.countrySelectionBtn.setOnClickListener {
            showCountrySelectionDialog(selectedCountries) {
                selectedCountries = it
                movie = movie.copy(countryCodes = it)
                ui.countrySelectionBtn.text = it.toString()
            }
        }
        ui.genreBtn.setOnClickListener {
            showMovieGenreSelectionDialog(selectedGenres) {
                selectedGenres = it
                movie = movie.copy(genres = it)
                ui.genreBtn.text = it.toString()
            }
        }

        ui.titleEt.categoryArg = requireArguments().getParcelableOf(ARG_CATEGORY)
        ui.titleEt.setOnItemChosenListener(object : SearchMovieAutoCompleteTextView.MovieObserver {
            override fun onChosen(mov: Movie) {
                ui.yearEt.setText(mov.year.toString())
                ui.posterUrlEt.setText(mov.posterUrl)

                movie = mov
                selectedCountries = mov.countryCodes
                ui.countrySelectionBtn.text = mov.countryCodes.toString()
                selectedGenres = mov.genres
                ui.genreBtn.text = mov.genres.toString()
            }
        })
    }
}
