package dev.liinahamari.suggestions_ui.movie

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import com.jakewharton.rxbinding4.widget.textChanges
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.core.ext.toast
import dev.liinahamari.suggestions_ui.PicturedArrayAdapter
import dev.liinahamari.suggestions_ui.SuggestionUi
import dev.liinahamari.suggestions_ui.movie.SearchMoviesViewModel.GetRemoteMovies
import dev.liinahamari.suggestions_ui.toUi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

const val ARG_CATEGORY = "search_movie_category"

/** To work properly, this custom View requires Fragment-holder to hold argument of [ARG_CATEGORY]
 *  It should be one of enum's [dev.liinahamari.api.domain.entities.Category] values
 *  For example, while instantiating your fragment, apply arguments to it:
 *  YourFragment().apply { arguments = bundleOf(ARG_CATEGORY to category)
 * */
open class SearchMovieAutoCompleteTextView @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.autoCompleteTextViewStyle
) : HideSuggestionListOnScrollMaterialAutoCompleteTextView(context, attributeSet, defStyleAttr) {
    protected open val viewModel by lazy { ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<SearchMoviesViewModel>() }
    private val suggestionsAdapter: PicturedArrayAdapter by lazy { PicturedArrayAdapter(context) }
    private val disposable = CompositeDisposable()
    private var suggestionsEnabled = true
    var categoryArg: Category = Category.GOOD

    private lateinit var movieObserver: MovieObserver

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (suggestionsEnabled) {
            setAdapter(suggestionsAdapter)
            disposable.add(textChanges()
                .filter { it.isBlank().not() }
                .map { it.toString() }
                .throttleLast(1300, TimeUnit.MILLISECONDS)
                .switchMap(::searchMovie)
                .subscribe())

            setupViewModelSubscriptions()
        } else {
            setAdapter(null)
        }
    }

    fun setSuggestionsEnabled(suggestionsEnabled: Boolean) {
        this.suggestionsEnabled = suggestionsEnabled
    }

    open fun searchMovie(query: String): Observable<GetRemoteMovies> = viewModel.searchForMovie(query, categoryArg)

    private fun setupViewModelSubscriptions() {
        viewModel.searchMoviesResultEvent.observe(findViewTreeLifecycleOwner()!!) {
            when (it) {
                is GetRemoteMovies.Error.CommonError -> context.toast("Suggestions API failed")
                is GetRemoteMovies.Error.NoInternetError -> context.toast("Check the Internet connection")
                is GetRemoteMovies.Success -> {
                    suggestionsAdapter.replaceAll(it.movies.map { it.toUi() })
                    setOnItemClickListener { _, _, position, _ -> movieObserver.onChosen(it.movies[position]) }
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        disposable.dispose()
        super.onDetachedFromWindow()
    }

    interface MovieObserver {
        fun onChosen(mov: Movie)
    }


    fun setOnItemChosenListener(mo: MovieObserver) {
        this.movieObserver = mo
    }

    override fun convertSelectionToString(selectedItem: Any?): CharSequence = (selectedItem as SuggestionUi).title
}
