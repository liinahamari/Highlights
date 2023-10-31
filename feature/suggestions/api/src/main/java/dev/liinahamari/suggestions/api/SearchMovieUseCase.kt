package dev.liinahamari.suggestions.api

import dev.liinahamari.suggestions.api.model.Search
import io.reactivex.rxjava3.core.Single

interface SearchMovieUseCase {
    fun search(searchParams: String): Single<List<Search>>
}
