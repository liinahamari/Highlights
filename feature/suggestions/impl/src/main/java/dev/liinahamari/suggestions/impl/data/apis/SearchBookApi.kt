package dev.liinahamari.suggestions.impl.data.apis

import dev.liinahamari.suggestions.impl.data.model.SearchBookResponse
import io.reactivex.rxjava3.core.Single

interface SearchBookApi {
    fun searchBookByTitle(title: String): Single<SearchBookResponse>
    fun searchBookByAuthor(authorsName: String): Single<SearchBookResponse>
}
