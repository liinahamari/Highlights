package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.content.DialogInterface.OnClickListener
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.BookGenre
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Country
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentAddBookBinding
import dev.liinahamari.list_ui.viewmodels.CachedCountriesViewModel
import dev.liinahamari.suggestions_ui.book.SearchBookAutoCompleteTextView
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY

class AddBookDialogFragment : AddFragment(R.layout.fragment_add_book) {
    private var _ui: FragmentAddBookBinding? = null
    private val ui: FragmentAddBookBinding by lazy { _ui!! }
    private val cachedCountriesViewModel: CachedCountriesViewModel by viewModels { viewModelFactory }

    private var book = Book.default()
    private var selectedCountries = listOf<Country>()
    private var selectedGenres = listOf<BookGenre>()

    companion object {
        fun newInstance(category: Category): AddBookDialogFragment = AddBookDialogFragment().apply {
            book = book.copy(category = category)
            arguments = bundleOf(ARG_CATEGORY to category)
        }
    }

    override fun onSaveButtonClicked() = OnClickListener { _, _ -> saveEntryViewModel.saveBook(book) }
    override fun webSearchQuery(): String = ui.titleEt.text.toString() + " book (${ui.yearEt.text})"
    override fun getDialogCustomView() = FragmentAddBookBinding.inflate(layoutInflater).also { _ui = it }.root

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    override fun setupViewModelSubscriptions() {
        super.setupViewModelSubscriptions()
        cachedCountriesViewModel.fetchCountriesEvent.observe(viewLifecycleOwner) {
            when (it) {
                is CachedCountriesViewModel.GetAllCountriesEvent.Failure -> toast("Failed to get countries' list")
                is CachedCountriesViewModel.GetAllCountriesEvent.Success -> {
                    showCountrySelectionDialog(preselectedLocales = selectedCountries, countries = it.countries) {
                        selectedCountries = it
                        book = book.copy(countries = it)
                        ui.countrySelectionBtn.text = it.joinToString { it.name }
                    }
                }
            }
        }
    }

    override fun setupTitleEditText() {
        ui.titleEt.isSuggestionsEnabled = preferenceRepo.suggestionsEnabled
        ui.titleEt.categoryArg = requireArguments().getParcelableOf(ARG_CATEGORY)
        ui.titleEt.setOnItemChosenListener(object : SearchBookAutoCompleteTextView.BookObserver {
            override fun onChosen(b: Book) {
                book = b
                ui.yearEt.setText(b.year.toString())
                ui.posterUrlEt.setText(b.posterUrl)
                selectedCountries = b.countries
                ui.countrySelectionBtn.text = b.countries.joinToString { it.name }
            }
        })
    }

    override fun setupSelectionDialogs() {
        ui.countrySelectionBtn.setOnClickListener {
            cachedCountriesViewModel.getCachedCountries()
        }
        ui.genreBtn.setOnClickListener {
            showBookGenreSelectionDialog(selectedGenres) {
                selectedGenres = it
                book = book.copy(genres = it)
                ui.genreBtn.text = it.toString()
            }
        }
    }

    override fun setupTextChangedListeners() {
        ui.authorEt.addTextChangedListener { book = book.copy(author = it.toString()) }
        ui.yearEt.addTextChangedListener { book = book.copy(year = it.toString().toInt()) }
        ui.posterUrlEt.addTextChangedListener { book = book.copy(posterUrl = it.toString()) }
        ui.titleEt.addTextChangedListener { book = book.copy(name = it.toString()) }
    }
}
