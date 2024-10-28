package dev.liinahamari.suggestions_ui.shorts

import android.app.Application
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.suggestions_ui.movie.SearchMoviesViewModel
import dev.liinahamari.suggestions_ui.movie.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.UnknownHostException

class SearchShortsViewModel(application: Application) : SearchMoviesViewModel(application) {
    fun searchForShort(
        query: String,
        category: Category
    ): Observable<GetRemoteMovies> =
        searchMovieUseCase.search(query)
            .flatMapObservable {
                Observable.fromIterable(it).flatMap {
                    searchMovieUseCase.getMovieDetails(it.remoteId!!).toObservable()
                        .map { response -> response.runtime to it }
                }
            }
            .filter { it.first in 3..30 }
            .map { it.second.toDomain(category, emptyList()) }
            .toList()
            .map<GetRemoteMovies>(GetRemoteMovies::Success)
            .onErrorReturn {
                when (it) {
                    is UnknownHostException -> GetRemoteMovies.Error.NoInternetError
                    else -> GetRemoteMovies.Error.CommonError
                }
            }
            .toObservable()
            .doOnError { it.printStackTrace() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(_searchMoviesEvent::setValue)
}
