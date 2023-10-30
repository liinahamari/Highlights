package dev.liinahamari.movies_suggestions.impl.usecases

import dev.liinahamari.movies_suggestions.api.SearchMovieUseCase
import dev.liinahamari.movies_suggestions.api.model.Search
import dev.liinahamari.movies_suggestions.impl.data.SearchRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchMovieUseCaseImpl @Inject constructor(private val searchRepo: SearchRepo) : SearchMovieUseCase {
    override fun search(searchParams: String): Single<List<Search>> = searchRepo.search(searchParams)
}
