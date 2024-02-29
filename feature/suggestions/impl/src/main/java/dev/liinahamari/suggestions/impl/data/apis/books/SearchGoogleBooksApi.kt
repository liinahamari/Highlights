package dev.liinahamari.suggestions.impl.data.apis.books

import dev.liinahamari.suggestions.api.model.books.BooksResource
import dev.liinahamari.suggestions.api.model.books.RemoteGoogleBook
import dev.liinahamari.suggestions.impl.BuildConfig
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchGoogleBooksApi {
    @GET("volumes")
    fun getAllBooks(
        @Query("q") query: String,
        @Query("key") key: String = BuildConfig.GOOGLE_BOOKS_API_KEY
    ): Single<BooksResource>

    @GET("volumes/{bookId}")
    fun getBookInfo(@Path("bookId") bookId: String): Single<RemoteGoogleBook>
}
