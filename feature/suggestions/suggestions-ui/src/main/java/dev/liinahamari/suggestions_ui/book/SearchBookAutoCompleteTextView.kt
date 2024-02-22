package dev.liinahamari.suggestions_ui.book

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.jakewharton.rxbinding4.widget.textChanges
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.toast
import dev.liinahamari.suggestions_ui.PicturedArrayAdapter
import dev.liinahamari.suggestions_ui.SuggestionUi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SearchBookAutoCompleteTextView @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.autoCompleteTextViewStyle
) : MaterialAutoCompleteTextView(context, attributeSet, defStyleAttr) {
    private val viewModel by lazy { ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<SearchBookViewModel>() }
    private val suggestionsAdapter: PicturedArrayAdapter by lazy { PicturedArrayAdapter(context) }
    private val disposable = CompositeDisposable()
    var categoryArg: Category = Category.GOOD //fixme actual

    private lateinit var bookObserver: BookObserver

    fun setOnItemChosenListener(bo: BookObserver) {
        this.bookObserver = bo
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setAdapter(suggestionsAdapter)
        disposable.add(textChanges()
            .filter { it.isBlank().not() }
            .map { it.toString() }
            .throttleLast(1300, TimeUnit.MILLISECONDS)
            .switchMap(::searchBook)
            .subscribe())

        setupViewModelSubscriptions()
    }

    private fun searchBook(query: String): Observable<GetRemoteBooks> = viewModel.searchBook(query, categoryArg)

    private fun setupViewModelSubscriptions() {
        viewModel.searchBookEvent.observe(findViewTreeLifecycleOwner()!!) {
            when (it) {
                is GetRemoteBooks.Error.CommonError -> context.toast("Suggestions API failed")
                is GetRemoteBooks.Error.NoInternetError -> context.toast("Check the Internet connection")

                is GetRemoteBooks.Success -> {
                    suggestionsAdapter.replaceAll(it.books.map {
                        SuggestionUi(
                            title = it.name,
                            year = it.year,
                            posterUrl = it.posterUrl,
                            genres = it.genres.map { it.name.replace("_", " ").lowercase() }
                        )
                    }
                    )
                    setOnItemClickListener { _, _, position, _ -> bookObserver.onChosen(it.books[position]) }
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        disposable.dispose()
        super.onDetachedFromWindow()
    }

    interface BookObserver {
        fun onChosen(b: Book)
    }

    override fun convertSelectionToString(selectedItem: Any?): CharSequence = (selectedItem as SuggestionUi).title
}
