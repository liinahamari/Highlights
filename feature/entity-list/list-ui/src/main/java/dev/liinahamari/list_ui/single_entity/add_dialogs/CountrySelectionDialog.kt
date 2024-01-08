package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import java.util.Locale
import java.util.Locale.getISOCountries

//todo move to first launch and cache

fun Context.showCountrySelectionDialog(onCountrySelected: (List<String>) -> Unit) {
    val allLocales = getISOCountries().sorted().map { Locale("", it) }

    MaterialDialog(this)
        .positiveButton(res = android.R.string.ok) { }
        .listItemsMultiChoice(items = allLocales.map { it.displayCountry },
            selection = { _: MaterialDialog, indices: IntArray, _: List<CharSequence> ->
                onCountrySelected.invoke(getISOCountries().slice(indices.toList()).map { Locale("", it).country })
            })
        .show()
}
