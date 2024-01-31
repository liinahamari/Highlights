package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import dev.liinahamari.core.ext.getSelectedIndices
import java.util.Locale
import java.util.Locale.getISOCountries

//todo move to first launch and cache

fun Context.showCountrySelectionDialog(selected: List<String>, onCountrySelected: (List<String>) -> Unit) {
    val allLocales = getISOCountries().map { Locale("", it) }.sortedBy { it.displayCountry }

    MaterialDialog(this)
        .positiveButton(res = android.R.string.ok) { }
        .listItemsMultiChoice(items = allLocales.map { it.displayCountry },
            initialSelection = allLocales.map { it.country }.getSelectedIndices(selected),
            selection = { _: MaterialDialog, indices: IntArray, _: List<CharSequence> ->
                onCountrySelected.invoke(allLocales.slice(indices.toList()).map { it.country })
            })
        .show()
}
