package dev.liinahamari.suggestions.api.usecases

import dev.liinahamari.suggestions.api.model.MovieDetailsResponse
import dev.liinahamari.suggestions.api.model.TmdbRemoteMovie
import io.reactivex.rxjava3.core.Single

interface SearchMovieUseCase {
    fun search(searchParams: String): Single<List<TmdbRemoteMovie>>
    fun getMovieDetails(remoteId: Long): Single<MovieDetailsResponse>
}
