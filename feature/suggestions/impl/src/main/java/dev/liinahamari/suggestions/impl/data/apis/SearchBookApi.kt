package dev.liinahamari.suggestions.impl.data.apis

import dev.liinahamari.suggestions.api.model.AUTHOR_ALTERNATIVE_NAME
import dev.liinahamari.suggestions.api.model.AUTHOR_KEY
import dev.liinahamari.suggestions.api.model.AUTHOR_NAME
import dev.liinahamari.suggestions.api.model.COVER_EDITION_KEY
import dev.liinahamari.suggestions.api.model.FIRST_PUBLISH_YEAR
import dev.liinahamari.suggestions.api.model.ISBN
import dev.liinahamari.suggestions.api.model.NUMBER_OF_PAGES_MEDIAN
import dev.liinahamari.suggestions.api.model.SUBJECT
import dev.liinahamari.suggestions.api.model.TITLE
import dev.liinahamari.suggestions.impl.data.model.SearchBookResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

private const val BOOK_SEARCH_REQUEST_LIMIT = 20
private const val REMOTE_BOOK_FIELDS =
    "$TITLE,$ISBN,$AUTHOR_KEY,$FIRST_PUBLISH_YEAR,$NUMBER_OF_PAGES_MEDIAN,$AUTHOR_NAME,$AUTHOR_ALTERNATIVE_NAME,$COVER_EDITION_KEY,$SUBJECT"

interface SearchBookApi {
    @GET("search.json?")
    fun searchBookByTitle(
        @Query("q") title: String,
        @Query("limit") limit: Int = BOOK_SEARCH_REQUEST_LIMIT,
        @Query("fields") fields: String = REMOTE_BOOK_FIELDS
    ): Single<SearchBookResponse>

    @GET("search/authors.json?q=")
    fun searchBookByAuthor(authorsName: String): Single<SearchBookResponse>
}
