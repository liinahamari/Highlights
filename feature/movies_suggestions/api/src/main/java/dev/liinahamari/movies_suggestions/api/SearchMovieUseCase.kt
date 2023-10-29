package dev.liinahamari.movies_suggestions.api

import dev.liinahamari.movies_suggestions.api.model.Search
import io.reactivex.rxjava3.core.Single

interface SearchMovieUseCase {
    fun search(searchParams: String): Single<List<Search>>
}
