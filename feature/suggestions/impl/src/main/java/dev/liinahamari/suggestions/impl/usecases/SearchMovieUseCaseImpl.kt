package dev.liinahamari.suggestions.impl.usecases

import dev.liinahamari.suggestions.api.model.TmdbRemoteMovie
import dev.liinahamari.suggestions.api.usecases.SearchMovieUseCase
import dev.liinahamari.suggestions.api.model.MovieDetailsResponse
import dev.liinahamari.suggestions.impl.data.repos.MovieRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchMovieUseCaseImpl @Inject constructor(private val movieRepo: MovieRepo) : SearchMovieUseCase {
    override fun search(searchParams: String): Single<List<TmdbRemoteMovie>> = movieRepo.search(searchParams)
    override fun getMovieDetails(remoteId: Long): Single<MovieDetailsResponse> = movieRepo.getMovieDetails(remoteId)
}
