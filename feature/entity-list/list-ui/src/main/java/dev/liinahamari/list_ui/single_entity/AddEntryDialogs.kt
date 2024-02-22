package dev.liinahamari.list_ui.single_entity

import android.webkit.URLUtil.isNetworkUrl
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.BookGenre
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.GameGenre
import dev.liinahamari.list_ui.R
import java.util.Locale
import java.util.Locale.getISOCountries

//todo forbid adding default entries
//todo checked on edit

fun Fragment.showAddGameDialog(
    category: Category,
    onSubmit: (game: Game) -> Unit,
    game: Game? = null
) {
    var game = game ?: Game.default(category)
    getGenericAddEntryDialog(onSubmit = { onSubmit.invoke(game) },
        countriesSelectionCallback = {},
        genresSelectionCallback = { game = game.copy(genres = it.map { GameGenre.valueOf(it.replace(' ', '_')) }) },
        genres = GameGenre.values().map { it.toString().replace('_', ' ') }).apply {
        findViewById<Button>(R.id.countrySelectionBtn)?.isVisible = false
        findViewById<TextInputEditText>(R.id.titleEt)
            ?.apply { setText(game.name) }
            ?.doOnTextChanged { text, _, _, _ -> game = game.copy(name = text.toString()) }
        findViewById<TextInputEditText>(R.id.yearEt)
            ?.apply { setText(game.year.toString()) }
            ?.doOnTextChanged { text, _, _, _ -> game = game.copy(year = text.toString().ifEmpty { "0" }.toInt()) }
        findViewById<TextInputEditText>(R.id.posterUrlEt)
            ?.apply { setText(game.posterUrl) }
            ?.doOnTextChanged { text, _, _, _ -> game = game.copy(posterUrl = text.toString()) }
    }.show()
}

private fun Fragment.getGenericAddEntryDialog(
    onSubmit: () -> Unit,
    countriesSelectionCallback: (IntArray) -> Unit,
    genresSelectionCallback: (List<String>) -> Unit,
    genres: List<String>?
): AlertDialog =
    AlertDialog.Builder(requireContext()).apply {
        setView(R.layout.fragment_add_movie)
        create()
        setPositiveButton(android.R.string.ok) { _, _ -> onSubmit.invoke() }
    }.show().apply {
        findViewById<Button>(R.id.countrySelectionBtn)
            ?.setOnClickListener {
                //todo move to first launch and cache
                val allLocales = getISOCountries().map { Locale("", it) }
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
