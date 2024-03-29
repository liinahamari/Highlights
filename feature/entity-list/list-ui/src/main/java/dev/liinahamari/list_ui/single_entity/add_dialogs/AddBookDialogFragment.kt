package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.content.DialogInterface.OnClickListener
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.BookGenre
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentAddBookBinding
import dev.liinahamari.suggestions_ui.book.SearchBookAutoCompleteTextView
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY

class AddBookDialogFragment : AddFragment(R.layout.fragment_add_book) {
    private var _ui: FragmentAddBookBinding? = null
    private val ui: FragmentAddBookBinding by lazy { _ui!! }

    private var book = Book.default()
    private var selectedCountries = listOf<String>()
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

    override fun setupTitleEditText() {
        ui.titleEt.isSuggestionsEnabled = preferenceRepo.suggestionsEnabled
        ui.titleEt.categoryArg = requireArguments().getParcelableOf(ARG_CATEGORY)
        ui.titleEt.setOnItemChosenListener(object : SearchBookAutoCompleteTextView.BookObserver {
            override fun onChosen(b: Book) {
                ui.yearEt.setText(b.year.toString())
                ui.posterUrlEt.setText(b.posterUrl)

                book = Book(
                    category = b.category,
                    year = b.year,
                    posterUrl = b.posterUrl,
                    countryCodes = b.countryCodes,
                    name = b.name,
                    author = b.author,
                    genres = b.genres,
                    description = b.description
                )
                selectedCountries = b.countryCodes
                ui.countrySelectionBtn.text = b.countryCodes.toString()
            }
        })
    }

    override fun setupSelectionDialogs() {
        ui.countrySelectionBtn.setOnClickListener {
            showCountrySelectionDialog(selectedCountries) {
                selectedCountries = it
                book = book.copy(countryCodes = it)
                ui.countrySelectionBtn.text = it.toString()
            }
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
