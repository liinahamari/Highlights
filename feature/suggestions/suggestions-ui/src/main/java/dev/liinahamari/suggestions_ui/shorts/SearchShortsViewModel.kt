package dev.liinahamari.suggestions_ui.shorts

import android.app.Application
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Country
import dev.liinahamari.suggestions_ui.movie.SearchMoviesViewModel
import dev.liinahamari.suggestions_ui.movie.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.UnknownHostException

private val SHORTS_RANGE_IN_MINUTES = 3..30

class SearchShortsViewModel(application: Application) : SearchMoviesViewModel(application) {

    fun searchForShort(
        query: String,
        category: Category
    ): Observable<GetRemoteMovies> =
        searchMovieUseCase.search(query)
            .flatMapObservable {
                Observable.fromIterable(it)
                    .flatMap { short ->
                        searchMovieUseCase.getMovieDetails(short.remoteId!!).toObservable()
                            .filter { detailsResponse -> detailsResponse.runtime in SHORTS_RANGE_IN_MINUTES }
                            .map { short.toDomain(category) }
                            .flatMapSingle { mov ->
                                searchMovieUseCase.getMovieDetails(short.remoteId!!)
                                    .map { response ->
                                        mov.copy(
                                            productionCountries = response.productionCountries.map {
                                                Country(iso = it.iso31661!!, name = it.name!!)
                                            },
                                        )
                                    }
                            }
                    }
            }
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
