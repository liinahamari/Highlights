package dev.liinahamari.suggestions.impl.data.apis

import dev.liinahamari.suggestions.impl.data.model.SearchBookResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchBookApi {
    @GET("search.json?")
    fun searchBookByTitle(
        @Query("q") title: String,
        @Query("limit") limit: Int = 20
    ): Single<SearchBookResponse>

    @GET("search/authors.json?q=")
    fun searchBookByAuthor(authorsName: String): Single<SearchBookResponse>
}
