package dev.liinahamari.suggestions.impl.data.apis

import dev.liinahamari.suggestions.impl.data.model.SearchBookResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface SearchBookApi {
    @GET("search.json?q=")
    fun searchBookByTitle(title: String): Single<SearchBookResponse>
    @GET("search/authors.json?q=")
    fun searchBookByAuthor(authorsName: String): Single<SearchBookResponse>
}
