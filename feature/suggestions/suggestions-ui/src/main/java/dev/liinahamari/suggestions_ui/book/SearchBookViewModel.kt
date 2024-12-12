package dev.liinahamari.suggestions_ui.book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.suggestions.MovieSuggestionsListFactory
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.suggestions.api.MovieSuggestionsDependencies
import dev.liinahamari.suggestions.api.model.books.RemoteGoogleBook
import dev.liinahamari.suggestions.api.model.books.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Predicate
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.UnknownHostException

class SearchBookViewModel(application: Application) : AndroidViewModel(application) {
    private val _searchBookEvent = SingleLiveEvent<GetRemoteBooks>()
    val searchBookEvent: LiveData<GetRemoteBooks> get() = _searchBookEvent

    private val api by lazy {
        MovieSuggestionsListFactory.getApi(object : MovieSuggestionsDependencies {
            override val application: Application
                get() = application
        })
    }

    private val searchBookUseCase by lazy { api.searchBookUseCase }

    fun searchBook(query: String, category: Category): Observable<GetRemoteBooks> =
        searchBookUseCase.searchGoogleBooks(query)
            .flatMapObservable { Observable.fromIterable(it) }
            .filter(verifyRemoteBookConsistency())
            .map { it.toDomain(category) }
            .toList()
            .toObservable()
            .map<GetRemoteBooks>(GetRemoteBooks::Success)
            .startWithItem(GetRemoteBooks.Loading)
            .doOnError { it.printStackTrace() } //fixme reorder in other viewmodels
            .onErrorReturn {
                when (it) {
                    is UnknownHostException -> GetRemoteBooks.Error.NoInternetError
                    else -> GetRemoteBooks.Error.CommonError
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(_searchBookEvent::setValue)

    private fun verifyRemoteBookConsistency() = Predicate<RemoteGoogleBook> {
        it.volumeInfo != null &&
                (it.volumeInfo!!.title.isNullOrBlank() || it.volumeInfo!!.title == "null").not()
                && it.volumeInfo!!.publishedDate.isNullOrBlank().not()
                && it.volumeInfo!!.authors.isNullOrEmpty().not()
                && it.volumeInfo!!.authors!!.first().isNullOrBlank().not()
    }
}

sealed interface GetRemoteBooks {
    data object Loading : GetRemoteBooks
    data class Success(val books: List<Book>) : GetRemoteBooks
    sealed interface Error : GetRemoteBooks {
        data object NoInternetError : Error
        data object CommonError : Error
    }
}
