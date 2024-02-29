package dev.liinahamari.suggestions.impl.data.repos

import dev.liinahamari.suggestions.api.model.books.RemoteGoogleBook
import dev.liinahamari.suggestions.api.model.books.RemoteOpenLibraryBook
import dev.liinahamari.suggestions.impl.data.apis.books.SearchGoogleBooksApi
import dev.liinahamari.suggestions.impl.data.apis.books.SearchOpenLibraryApi
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface BookRepo {
    fun searchOpenLibrary(searchParams: String): Single<List<RemoteOpenLibraryBook>>
    fun searchGoogleBooks(searchParams: String): Maybe<List<RemoteGoogleBook>>
}

class BookRepoImpl @Inject constructor(
    private val gApi: SearchGoogleBooksApi,
    private val olApi: SearchOpenLibraryApi
) : BookRepo {
    override fun searchOpenLibrary(searchParams: String): Single<List<RemoteOpenLibraryBook>> =
        olApi.searchBookByTitle(searchParams).map { it.docs }

    override fun searchGoogleBooks(searchParams: String): Maybe<List<RemoteGoogleBook>> = gApi.getAllBooks(searchParams)
        .filter { it.remoteGoogleBooks.isNullOrEmpty().not() && it.remoteGoogleBooks!!.filterNotNull().isEmpty().not() }
        .map { it.remoteGoogleBooks!!.filterNotNull() }
}
