package dev.liinahamari.suggestions.impl.data.repos

import dev.liinahamari.suggestions.api.model.RemoteMovie
import dev.liinahamari.suggestions.impl.data.apis.SearchMovieApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface MovieRepo {
    fun search(searchParams: String): Single<List<RemoteMovie>>
}

class MovieRepoImpl @Inject constructor(
    private val api: SearchMovieApi
) : MovieRepo {
    override fun search(searchParams: String): Single<List<RemoteMovie>> = api.search(searchParams).map { it.results }
}
