package dev.liinahamari.movies_suggestions.impl.data

import dev.liinahamari.movies_suggestions.api.model.Search
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface SearchRepo {
    fun multiSearch(searchParams: String): Single<List<Search>>
}

class SearchRepoImpl @Inject constructor(
    private val api: SearchMovieApi
) : SearchRepo {
    override fun multiSearch(searchParams: String): Single<List<Search>> =
        api.multiSearch(searchParams).map { it.results }
}
