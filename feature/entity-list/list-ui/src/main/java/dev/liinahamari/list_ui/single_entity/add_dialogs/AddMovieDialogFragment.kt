package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.content.DialogInterface
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.api.domain.entities.MovieGenre
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentAddMovieBinding
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY
import dev.liinahamari.suggestions_ui.movie.SearchMovieAutoCompleteTextView

class AddMovieDialogFragment : AddFragment(R.layout.fragment_add_movie) {
    private var _ui: FragmentAddMovieBinding? = null
    private val ui: FragmentAddMovieBinding by lazy { _ui!! }

    private var selectedCountries = listOf<String>()
    private var selectedGenres = listOf<MovieGenre>()
    private var movie = Movie.default()

    companion object {
        fun newInstance(category: Category): AddMovieDialogFragment = AddMovieDialogFragment().apply {
            movie = movie.copy(category = category)
            arguments = bundleOf(ARG_CATEGORY to category)
        }
    }

    override fun webSearchQuery(): String = ui.titleEt.text.toString() + " film (${ui.yearEt.text})"
    override fun onSaveButtonClicked() = DialogInterface.OnClickListener { _, _ -> saveEntryViewModel.saveMovie(movie) }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    override fun getDialogCustomView() = FragmentAddMovieBinding.inflate(layoutInflater).also { _ui = it }.root

    override fun setupTitleEditText() {
        ui.titleEt.categoryArg = requireArguments().getParcelableOf(ARG_CATEGORY)
        ui.titleEt.setOnItemChosenListener(object : SearchMovieAutoCompleteTextView.MovieObserver {
            override fun onChosen(mov: Movie) {
                ui.yearEt.setText(mov.releaseYear.toString())
                ui.posterUrlEt.setText(mov.posterUrl)

                movie = mov
                selectedCountries = mov.productionCountries
                ui.countrySelectionBtn.text = mov.productionCountries.toString()
                selectedGenres = mov.genres
                ui.genreBtn.text = mov.genres.toString()
            }
        })
    }

    override fun setupSelectionDialogs() {
        ui.countrySelectionBtn.setOnClickListener {
            showCountrySelectionDialog(selectedCountries) {
                selectedCountries = it
                movie = movie.copy(productionCountries = it)
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
    }

    override fun setupTextChangedListeners() {
        ui.yearEt.addTextChangedListener { movie = movie.copy(releaseYear = it.toString().toInt()) }
        ui.posterUrlEt.addTextChangedListener { movie = movie.copy(posterUrl = it.toString()) }
        ui.titleEt.addTextChangedListener { movie = movie.copy(title = it.toString()) }
    }
}
