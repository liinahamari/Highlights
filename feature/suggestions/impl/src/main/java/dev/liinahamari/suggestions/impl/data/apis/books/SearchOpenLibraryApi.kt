package dev.liinahamari.suggestions.impl.data.apis.books

import dev.liinahamari.suggestions.api.model.books.AUTHOR_ALTERNATIVE_NAME
import dev.liinahamari.suggestions.api.model.books.AUTHOR_KEY
import dev.liinahamari.suggestions.api.model.books.AUTHOR_NAME
import dev.liinahamari.suggestions.api.model.books.COVER_EDITION_KEY
import dev.liinahamari.suggestions.api.model.books.FIRST_PUBLISH_YEAR
import dev.liinahamari.suggestions.api.model.books.ISBN
import dev.liinahamari.suggestions.api.model.books.NUMBER_OF_PAGES_MEDIAN
import dev.liinahamari.suggestions.api.model.books.SUBJECT
import dev.liinahamari.suggestions.api.model.books.TITLE
import dev.liinahamari.suggestions.impl.data.model.SearchBookResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

private const val BOOK_SEARCH_REQUEST_LIMIT = 20
private const val REMOTE_BOOK_FIELDS =
    "$TITLE,$ISBN,$AUTHOR_KEY,$FIRST_PUBLISH_YEAR,$NUMBER_OF_PAGES_MEDIAN,$AUTHOR_NAME,$AUTHOR_ALTERNATIVE_NAME,$COVER_EDITION_KEY,$SUBJECT"
interface SearchOpenLibraryApi {
    @GET("search.json?")
    fun searchBookByTitle(
        @Query("q") title: String,
        @Query("limit") limit: Int = BOOK_SEARCH_REQUEST_LIMIT,
        @Query("fields") fields: String = REMOTE_BOOK_FIELDS
    ): Single<SearchBookResponse>

    @GET("search/authors.json?q=")
    fun searchBookByAuthor(authorsName: String): Single<SearchBookResponse>
}
