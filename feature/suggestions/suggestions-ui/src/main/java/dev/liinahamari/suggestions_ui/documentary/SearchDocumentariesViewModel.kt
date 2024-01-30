package dev.liinahamari.suggestions_ui.documentary

import android.app.Application
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.suggestions_ui.movie.SearchMoviesViewModel
import dev.liinahamari.suggestions_ui.movie.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.UnknownHostException

private const val IMDB_DOCUMENTARY_GENRE_ID = 99

class SearchDocumentariesViewModel(application: Application) : SearchMoviesViewModel(application) {
    fun searchForDocumentary(
        query: String,
        category: Category
    ): Observable<GetRemoteMovies> =
        searchMovieUseCase.search(query)
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { it.genreIds!!.contains(IMDB_DOCUMENTARY_GENRE_ID) }
            .map { it.toDomain(category, emptyList()) }
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
