package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.BookGenre
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.repo.PreferencesRepo
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.databinding.FragmentAddBookBinding
import dev.liinahamari.list_ui.viewmodels.SaveEntryViewModel
import dev.liinahamari.list_ui.viewmodels.SaveEvent
import dev.liinahamari.suggestions_ui.book.SearchBookAutoCompleteTextView
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY
import javax.inject.Inject

class AddBookDialogFragment : DialogFragment(R.layout.fragment_add_book) {
    private var _ui: FragmentAddBookBinding? = null
    private val ui: FragmentAddBookBinding by lazy { _ui!! }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var preferenceRepo: PreferencesRepo
    private val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }

    private var book = Book.default()

    companion object {
        fun newInstance(category: Category): AddBookDialogFragment = AddBookDialogFragment().apply {
            arguments = bundleOf(ARG_CATEGORY to category)
        }
    }

    override fun onAttach(context: Context) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
        .setView((FragmentAddBookBinding.inflate(layoutInflater)).also { _ui = it }.root)
        .setNeutralButton(R.string.internet_search) { _, _ -> }
        .setPositiveButton(R.string.save) { _, _ -> saveEntryViewModel.saveBook(book) }
        .create()
        .apply {
            setOnShowListener {
                (dialog as AlertDialog).getButton(DialogInterface.BUTTON_NEUTRAL)
                    .setOnClickListener {
                        startActivity(
                            Intent(Intent.ACTION_WEB_SEARCH)
                                .putExtra(SearchManager.QUERY, ui.titleEt.text.toString() + " book (${ui.yearEt.text})")
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
            is SaveEvent.Failure -> toast("Failed to save book")
            is SaveEvent.Success -> requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun setupUi() {
        var selectedCountries = listOf<String>()
        var selectedGenres = listOf<BookGenre>()

        ui.titleEt.isSuggestionsEnabled = preferenceRepo.suggestionsEnabled

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
}

