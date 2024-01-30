package dev.liinahamari.suggestions_ui.documentary

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import dev.liinahamari.suggestions_ui.movie.SearchMovieAutoCompleteTextView
import dev.liinahamari.suggestions_ui.movie.SearchMoviesViewModel
import io.reactivex.rxjava3.core.Observable

class SearchDocumentaryAutoCompleteTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.autoCompleteTextViewStyle
) : SearchMovieAutoCompleteTextView(context, attributeSet, defStyleAttr) {
    override val viewModel: SearchDocumentariesViewModel by lazy { ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<SearchDocumentariesViewModel>() }

    override fun searchMovie(query: String): Observable<SearchMoviesViewModel.GetRemoteMovies> =
        viewModel.searchForDocumentary(query, categoryArg)
}
