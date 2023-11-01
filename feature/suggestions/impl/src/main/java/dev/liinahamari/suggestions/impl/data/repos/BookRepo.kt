package dev.liinahamari.suggestions.impl.data.repos

import dev.liinahamari.suggestions.api.model.RemoteBook
import dev.liinahamari.suggestions.impl.data.apis.SearchBookApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface BookRepo {
    fun search(searchParams: String): Single<List<RemoteBook>>
}

class BookRepoImpl @Inject constructor(
    private val api: SearchBookApi
) : BookRepo {
    override fun search(searchParams: String): Single<List<RemoteBook>> =
        api.searchBookByTitle(searchParams).map { it.docs }
}
