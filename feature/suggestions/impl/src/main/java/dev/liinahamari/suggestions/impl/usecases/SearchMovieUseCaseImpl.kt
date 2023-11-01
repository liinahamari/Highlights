package dev.liinahamari.suggestions.impl.usecases

import dev.liinahamari.suggestions.api.usecases.SearchMovieUseCase
import dev.liinahamari.suggestions.api.model.RemoteMovie
import dev.liinahamari.suggestions.impl.data.repos.MovieRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchMovieUseCaseImpl @Inject constructor(private val movieRepo: MovieRepo) : SearchMovieUseCase {
    override fun search(searchParams: String): Single<List<RemoteMovie>> = movieRepo.search(searchParams)
}
