package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.content.DialogInterface.OnClickListener
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentAddDocumentaryBinding
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY
import dev.liinahamari.suggestions_ui.movie.SearchMovieAutoCompleteTextView

class AddDocumentaryDialogFragment : AddFragment(R.layout.fragment_add_documentary) {
    private var _ui: FragmentAddDocumentaryBinding? = null
    private val ui: FragmentAddDocumentaryBinding by lazy { _ui!! }

    private var documentary = Documentary.default(Category.GOOD)
    private var selectedCountries = listOf<String>()

    companion object {
        fun newInstance(category: Category): AddDocumentaryDialogFragment = AddDocumentaryDialogFragment().apply {
            documentary = documentary.copy(category = category)
            arguments = bundleOf(ARG_CATEGORY to category)
        }
    }

    override fun getDialogCustomView() = FragmentAddDocumentaryBinding.inflate(layoutInflater).also { _ui = it }.root
    override fun onSaveButtonClicked() = OnClickListener { _, _ -> saveEntryViewModel.saveDocumentary(documentary) }
    override fun webSearchQuery(): String = ui.titleEt.text.toString() + " documentary (${ui.yearEt.text})"

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    override fun setupTitleEditText() {
        ui.titleEt.isSuggestionsEnabled = preferenceRepo.suggestionsEnabled

        ui.titleEt.categoryArg = requireArguments().getParcelableOf(ARG_CATEGORY)
        ui.titleEt.setOnItemChosenListener(object : SearchMovieAutoCompleteTextView.MovieObserver {
            override fun onChosen(mov: Movie) {
                ui.yearEt.setText(mov.releaseYear.toString())
                ui.posterUrlEt.setText(mov.posterUrl)

                documentary = Documentary(
                    category = mov.category,
                    year = mov.releaseYear,
                    posterUrl = mov.posterUrl,
                    countryCodes = mov.productionCountries,
                    name = mov.title,
                    description = mov.description,
                    tmdbUrl = mov.tmdbUrl
                )
                selectedCountries = mov.productionCountries
                ui.countrySelectionBtn.text = mov.productionCountries.toString()
            }
        })
    }

    override fun setupSelectionDialogs() {
        ui.countrySelectionBtn.setOnClickListener {
            showCountrySelectionDialog(selectedCountries) {
                selectedCountries = it
                documentary = documentary.copy(countryCodes = it)
                ui.countrySelectionBtn.text = it.toString()
            }
        }
    }

    override fun setupTextChangedListeners() {
        ui.yearEt.addTextChangedListener { documentary = documentary.copy(year = it.toString().toInt()) }
        ui.posterUrlEt.addTextChangedListener { documentary = documentary.copy(posterUrl = it.toString()) }
        ui.titleEt.addTextChangedListener { documentary = documentary.copy(name = it.toString()) }
    }
}

