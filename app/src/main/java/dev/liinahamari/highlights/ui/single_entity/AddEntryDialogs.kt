package dev.liinahamari.highlights.ui.single_entity

import android.webkit.URLUtil.isNetworkUrl
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.db.daos.Book
import dev.liinahamari.highlights.db.daos.BookGenre
import dev.liinahamari.highlights.db.daos.Documentary
import dev.liinahamari.highlights.db.daos.Game
import dev.liinahamari.highlights.db.daos.GameGenre
import dev.liinahamari.highlights.db.daos.Movie
import dev.liinahamari.highlights.db.daos.MovieGenre
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.toCompletable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale

//todo forbid adding default entries

fun Fragment.showAddMovieDialog(category: EntityCategory, onSubmit: (movie: Movie) -> Unit, movie: Movie? = null) {
    var movie = movie ?: Movie("", listOf(), 0, category, "", arrayOf())
    getGenericAddEntryDialog(
        onSubmit = { onSubmit.invoke(movie) },
        genresSelectionCallback = { items -> movie = movie.copy(genres = items.map { MovieGenre.valueOf(it.replace(' ', '_')) }) },
        countriesSelectionCallback = {
            movie = movie.copy(countryCodes = Locale.getISOCountries().slice(it.toList()).map { Locale("", it).country }
                .toTypedArray())
        },
        genres = MovieGenre.values().map { it.toString().replace('_', ' ') }).apply {
        findViewById<TextInputEditText>(R.id.nameEt)
            ?.doOnTextChanged { text, _, _, _ -> movie = movie.copy(name = text.toString()) }
        findViewById<TextInputEditText>(R.id.yearEt)
            ?.doOnTextChanged { text, _, _, _ -> movie = movie.copy(year = text.toString().toInt()) }
        findViewById<TextInputEditText>(R.id.posterUrlEt)
            ?.doOnTextChanged { text, _, _, _ ->
                Glide.with(this@showAddMovieDialog).load(text.toString()).downloadOnly(50, 50).toCompletable()
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                        Toast.makeText(context, "A", Toast.LENGTH_SHORT).show()
                    }, { Toast.makeText(context, "B", Toast.LENGTH_SHORT).show() })
                findViewById<TextInputLayout>(R.id.posterUrlInputLayout)?.error = null
                movie = movie.copy(posterUrl = text.toString())
            }
    }.show()
}

fun Fragment.showAddDocumentaryDialog(
    category: EntityCategory,
    onSubmit: (documentary: Documentary) -> Unit,
    documentary: Documentary? = null
) {
    var documentary = documentary ?: Documentary("", 0, category, "", arrayOf())
    getGenericAddEntryDialog(onSubmit = { onSubmit.invoke(documentary) }, countriesSelectionCallback = {
        documentary =
            documentary.copy(
                countryCodes = Locale.getISOCountries().slice(it.toList()).map { Locale("", it).country }
                    .toTypedArray())
    }, genresSelectionCallback = {},
        genres = null
    ).apply {
        findViewById<Button>(R.id.genreBtn)?.isGone = true
        findViewById<TextInputEditText>(R.id.nameEt)
            ?.doOnTextChanged { text, _, _, _ -> documentary = documentary.copy(name = text.toString()) }
        findViewById<TextInputEditText>(R.id.yearEt)
            ?.doOnTextChanged { text, _, _, _ -> documentary = documentary.copy(year = text.toString().toInt()) }
        findViewById<TextInputEditText>(R.id.posterUrlEt)
            ?.doOnTextChanged { text, _, _, _ -> documentary = documentary.copy(posterUrl = text.toString()) }
    }.show()
}

fun Fragment.showAddGameDialog(category: EntityCategory, onSubmit: (game: Game) -> Unit, game: Game? = null) {
    var game = game ?: Game("", listOf(), 0, category, "", arrayOf())
    getGenericAddEntryDialog(onSubmit = { onSubmit.invoke(game) },
        countriesSelectionCallback = {}, genresSelectionCallback = {
            game = game.copy(genres = it.map { GameGenre.valueOf(it.replace(' ', '_')) })
        }, genres = GameGenre.values().map { it.toString().replace('_', ' ') }).apply {
        findViewById<Button>(R.id.countrySelectionBtn)?.isVisible = false
        findViewById<TextInputEditText>(R.id.nameEt)
            ?.doOnTextChanged { text, _, _, _ -> game = game.copy(name = text.toString()) }
        findViewById<TextInputEditText>(R.id.yearEt)
            ?.doOnTextChanged { text, _, _, _ -> game = game.copy(year = text.toString().toInt()) }
        findViewById<TextInputEditText>(R.id.posterUrlEt)
            ?.doOnTextChanged { text, _, _, _ -> game = game.copy(posterUrl = text.toString()) }
    }.show()
}

fun Fragment.showAddBookDialog(category: EntityCategory, onSubmit: (book: Book) -> Unit, book: Book? = null) {
    var book = book ?: Book("", listOf(), "", 0, category, "", arrayOf())
    getGenericAddEntryDialog(onSubmit = { onSubmit.invoke(book) }, countriesSelectionCallback = {
        book = book.copy(countryCodes = Locale.getISOCountries().slice(it.toList()).map { Locale("", it).country }
            .toTypedArray())
    }, genresSelectionCallback = {
        book = book.copy(genres = it.map { BookGenre.valueOf(it.replace(' ', '_')) })
    }, genres = BookGenre.values().map { it.toString().replace('_', ' ') }).apply {
        findViewById<TextInputEditText>(R.id.nameEt)
            ?.apply { setText(book.name) }
            ?.doOnTextChanged { text, _, _, _ -> book = book.copy(name = text.toString()) }
        findViewById<TextInputLayout>(R.id.authorEtContainer)?.isVisible = true
        findViewById<TextInputEditText>(R.id.authorEt)
            ?.apply { setText(book.author) }
            ?.doOnTextChanged { text, _, _, _ -> book = book.copy(author = text.toString()) }
        findViewById<TextInputEditText>(R.id.yearEt)
            ?.apply { setText(book.year.toString()) }
            ?.doOnTextChanged { text, _, _, _ -> book = book.copy(year = text.toString().toInt()) }
        findViewById<TextInputEditText>(R.id.posterUrlEt)
            ?.apply { setText(book.posterUrl) }
            ?.doOnTextChanged { text, _, _, _ -> book = book.copy(posterUrl = text.toString()) }
    }.show()
}

private fun Fragment.getGenericAddEntryDialog(
    onSubmit: () -> Unit,
    countriesSelectionCallback: (IntArray) -> Unit,
    genresSelectionCallback: (List<String>) -> Unit,
    genres: List<String>?
): AlertDialog =
    AlertDialog.Builder(requireContext()).apply {
        setView(R.layout.add_entry_dialog)
        create()
        setPositiveButton(android.R.string.ok) { _, _ -> onSubmit.invoke() }
    }.show().apply {
        findViewById<Button>(R.id.countrySelectionBtn)
            ?.setOnClickListener {
                //todo move to first launch and cache
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

        findViewById<Button>(R.id.genreBtn)
            ?.setOnClickListener {
                MaterialDialog(context)
                    .listItemsMultiChoice(
                        items = genres,
                        selection = { _: MaterialDialog, _: IntArray, items: List<CharSequence>/*todo check _ handles fine*/ ->
                            genresSelectionCallback.invoke(items.map { it.toString() })
                        })
                    .positiveButton()
                    .show()
            }
    }
/*todo validation sm kupi*/
