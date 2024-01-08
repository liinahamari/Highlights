package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import dev.liinahamari.api.domain.entities.MovieGenre

fun Context.showMovieGenreSelectionDialog(genresSelectionCallback: (List<MovieGenre>) -> Unit) = MaterialDialog(this)
    .listItemsMultiChoice(
        items = MovieGenre.values().map { toString().replace("_", " ") },
        selection = { _: MaterialDialog, _: IntArray, items: List<CharSequence>/*todo check _ handles fine*/ ->
            genresSelectionCallback.invoke(items.map { it.toString() }.map { MovieGenre.valueOf(it.replace(' ', '_')) })
        })
    .positiveButton()
    .show()
