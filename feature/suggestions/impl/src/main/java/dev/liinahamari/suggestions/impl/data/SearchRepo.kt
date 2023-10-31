package dev.liinahamari.suggestions.impl.data

import dev.liinahamari.suggestions.api.model.Search
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface SearchRepo {
    fun search(searchParams: String): Single<List<Search>>
}

class SearchRepoImpl @Inject constructor(
    private val api: SearchMovieApi
) : SearchRepo {
    override fun search(searchParams: String): Single<List<Search>> = api.search(searchParams).map { it.results }
}
