package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import dev.liinahamari.api.domain.entities.MovieGenre
import dev.liinahamari.core.ext.getSelectedIndices

fun Context.showMovieGenreSelectionDialog(
    selected: List<MovieGenre>,
    genresSelectionCallback: (List<MovieGenre>) -> Unit
) = MaterialDialog(this)
    .listItemsMultiChoice(
        items = MovieGenre.values().map { it.name.replace("_", " ") },
        initialSelection = MovieGenre.values().getSelectedIndices(selected),
        selection = { _: MaterialDialog, _: IntArray, items: List<CharSequence> ->
            genresSelectionCallback.invoke(items.map { it.toString() }.map { MovieGenre.valueOf(it.replace(' ', '_')) })
        })
    .positiveButton()
    .show()
