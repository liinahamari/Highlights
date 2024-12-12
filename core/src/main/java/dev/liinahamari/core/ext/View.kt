package dev.liinahamari.core.ext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView

val View.keyboardIsVisible: Boolean
    get() = WindowInsetsCompat
        .toWindowInsetsCompat(rootWindowInsets)
        .isVisible(WindowInsetsCompat.Type.ime())

fun View.hideKeyboard() = context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)

fun View.toast(text: String) = Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
fun Fragment.toast(text: String) = requireContext().toast(text)

fun RecyclerView.createSelectionTracker(
    selectionObserverFunc: (Selection<Long>) -> Unit,
    tag: String,
    detailsLookup: ItemDetailsLookup<Long>
): SelectionTracker<Long> = SelectionTracker.Builder(
    tag,
    this,
    StableIdKeyProvider(this),
    detailsLookup,
    StorageStrategy.createLongStorage()
).withSelectionPredicate(
    SelectionPredicates.createSelectAnything()
).build().apply {
    addObserver(object : SelectionTracker.SelectionObserver<Long>() {
        override fun onSelectionChanged() {
            selectionObserverFunc(selection)
        }
    })
}
