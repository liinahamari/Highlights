package dev.liinahamari.highlights.ui.main

import android.webkit.URLUtil.isNetworkUrl
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.db.daos.Book
import dev.liinahamari.highlights.db.daos.Documentary
import dev.liinahamari.highlights.db.daos.Game
import dev.liinahamari.highlights.db.daos.Movie
import java.util.Locale

//todo forbid adding default entries

fun Fragment.showAddMovieDialog(category: EntityCategory, onSubmit: (movie: Movie) -> Unit) {
    var movie = Movie("", "", 0, category, "", arrayOf())
    getGenericAddEntryDialog({ onSubmit.invoke(movie) }) {
        movie = movie.copy(countryCodes = Locale.getISOCountries().slice(it.toList()).map { Locale("", it).country }
            .toTypedArray())
    }.apply {
        findViewById<TextInputEditText>(R.id.nameEt)
            ?.doOnTextChanged { text, _, _, _ -> movie = movie.copy(name = text.toString()) }
        findViewById<TextInputEditText>(R.id.genreEt)
            ?.doOnTextChanged { text, _, _, _ -> movie = movie.copy(genre = text.toString()) }
        findViewById<TextInputEditText>(R.id.yearEt)
            ?.doOnTextChanged { text, _, _, _ -> movie = movie.copy(year = text.toString().toInt()) }
        findViewById<TextInputEditText>(R.id.posterUrlEt)
            ?.doOnTextChanged { text, _, _, _ ->
                findViewById<TextInputLayout>(R.id.posterUrlInputLayout)?.error = null
                movie = movie.copy(posterUrl = text.toString())
            }
    }.show()
}

fun Fragment.showAddDocumentaryDialog(category: EntityCategory, onSubmit: (documentary: Documentary) -> Unit) {
    var documentary = Documentary("", 0, category, "", arrayOf())
    getGenericAddEntryDialog({ onSubmit.invoke(documentary) }) {
        documentary =
            documentary.copy(countryCodes = Locale.getISOCountries().slice(it.toList()).map { Locale("", it).country }
                .toTypedArray())
    }.apply {
        findViewById<TextInputEditText>(R.id.genreEt)?.isGone = true
        findViewById<TextInputEditText>(R.id.nameEt)
            ?.doOnTextChanged { text, _, _, _ -> documentary = documentary.copy(name = text.toString()) }
        findViewById<TextInputEditText>(R.id.yearEt)
            ?.doOnTextChanged { text, _, _, _ -> documentary = documentary.copy(year = text.toString().toInt()) }
        findViewById<TextInputEditText>(R.id.posterUrlEt)
            ?.doOnTextChanged { text, _, _, _ -> documentary = documentary.copy(posterUrl = text.toString()) }
    }.show()
}

fun Fragment.showAddGameDialog(category: EntityCategory, onSubmit: (game: Game) -> Unit) {
    var game = Game("", "", 0, category, "", arrayOf())
    getGenericAddEntryDialog({ onSubmit.invoke(game) }) {
        game = game.copy(countryCodes = Locale.getISOCountries().slice(it.toList()).map { Locale("", it).country }
            .toTypedArray())
    }.apply {
        findViewById<TextInputEditText>(R.id.nameEt)
            ?.doOnTextChanged { text, _, _, _ -> game = game.copy(name = text.toString()) }
        findViewById<TextInputEditText>(R.id.genreEt)
            ?.doOnTextChanged { text, _, _, _ -> game = game.copy(genre = text.toString()) }
        findViewById<TextInputEditText>(R.id.yearEt)
            ?.doOnTextChanged { text, _, _, _ -> game = game.copy(year = text.toString().toInt()) }
        findViewById<TextInputEditText>(R.id.posterUrlEt)
            ?.doOnTextChanged { text, _, _, _ -> game = game.copy(posterUrl = text.toString()) }
    }.show()
}

fun Fragment.showAddBookDialog(category: EntityCategory, onSubmit: (book: Book) -> Unit) {
    var book = Book("", "", "", 0, category, "", arrayOf())
    getGenericAddEntryDialog({ onSubmit.invoke(book) }) {
        book = book.copy(countryCodes = Locale.getISOCountries().slice(it.toList()).map { Locale("", it).country }
            .toTypedArray())
    }.apply {
        findViewById<TextInputEditText>(R.id.nameEt)
            ?.doOnTextChanged { text, _, _, _ -> book = book.copy(name = text.toString()) }
        findViewById<TextInputEditText>(R.id.authorTv)
            ?.doOnTextChanged { text, _, _, _ -> book = book.copy(author = text.toString()) }
        findViewById<TextInputEditText>(R.id.genreEt)
            ?.doOnTextChanged { text, _, _, _ -> book = book.copy(genre = text.toString()) }
        findViewById<TextInputEditText>(R.id.yearEt)
            ?.doOnTextChanged { text, _, _, _ -> book = book.copy(year = text.toString().toInt()) }
        findViewById<TextInputEditText>(R.id.posterUrlEt)
            ?.doOnTextChanged { text, _, _, _ -> book = book.copy(posterUrl = text.toString()) }
    }.show()
}

private fun Fragment.getGenericAddEntryDialog(
    onSubmit: () -> Unit,
    countriesSelectionCallback: (IntArray) -> Unit
): AlertDialog =
    AlertDialog.Builder(requireContext()).apply {
        setView(R.layout.add_entry_dialog)
        create()
        setPositiveButton(android.R.string.ok) { _, _ -> onSubmit.invoke() }
    }.show().apply {
        findViewById<Button>(R.id.countrySelectionBtn)
            ?.setOnClickListener {
                val allLocales = Locale.getISOCountries().map { Locale("", it) }
                MaterialDialog(context).positiveButton(res = android.R.string.ok) { }
                    .listItemsMultiChoice(items = allLocales.map { it.displayCountry },
                        selection = { _: MaterialDialog, indices: IntArray, _: List<CharSequence> ->
                            countriesSelectionCallback.invoke(indices)
                        }).show()
            }

        findViewById<TextInputEditText>(R.id.posterUrlEt)?.setOnFocusChangeListener { _, hasFocus: Boolean ->
            if (findViewById<TextInputEditText>(R.id.posterUrlEt)?.text.isNullOrEmpty() || hasFocus) return@setOnFocusChangeListener
            if (isNetworkUrl(findViewById<TextInputEditText>(R.id.posterUrlEt)?.text.toString())) {
                findViewById<TextInputLayout>(R.id.posterUrlInputLayout)?.error = null
            } else {
                findViewById<TextInputLayout>(R.id.posterUrlInputLayout)?.error = getString(R.string.url_is_not_valid)
            }
        }
    }
