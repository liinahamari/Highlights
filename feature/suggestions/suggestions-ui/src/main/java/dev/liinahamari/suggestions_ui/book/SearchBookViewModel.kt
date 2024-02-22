package dev.liinahamari.suggestions_ui.book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.suggestions.MovieSuggestionsListFactory
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.suggestions.api.MovieSuggestionsDependencies
import dev.liinahamari.suggestions.api.model.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
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
        searchBookUseCase.search(query)
            .flatMapObservable { Observable.fromIterable(it) }
            .filter {
                (it.title.isBlank() || it.title == "null").not()
                        && it.firstPublishYear != 0
                        && it.authorName.isNullOrEmpty().not() && it.authorName!!.first().isNullOrBlank().not()
            }
            .map { it.toDomain(category) }
            .toList()
            .map<GetRemoteBooks>(GetRemoteBooks::Success)
            .doOnError { it.printStackTrace() } //fixme reorder in other viewmodels
            .onErrorReturn {
                when (it) {
                    is UnknownHostException -> GetRemoteBooks.Error.NoInternetError
                    else -> GetRemoteBooks.Error.CommonError
                }
            }
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(_searchBookEvent::setValue)
}

sealed interface GetRemoteBooks {
    data class Success(val books: List<Book>) : GetRemoteBooks
    sealed interface Error : GetRemoteBooks {
        object NoInternetError : Error
        object CommonError : Error
    }
}
