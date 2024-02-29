package dev.liinahamari.suggestions.impl.usecases

import dev.liinahamari.suggestions.api.model.books.RemoteGoogleBook
import dev.liinahamari.suggestions.api.model.books.RemoteOpenLibraryBook
import dev.liinahamari.suggestions.api.usecases.SearchBookUseCase
import dev.liinahamari.suggestions.impl.data.repos.BookRepo
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchBookUseCaseImpl @Inject constructor(private val bookRepo: BookRepo) : SearchBookUseCase {
    override fun searchOpenLibrary(searchParams: String): Single<List<RemoteOpenLibraryBook>> = bookRepo.searchOpenLibrary(searchParams)
    override fun searchGoogleBooks(searchParams: String): Maybe<List<RemoteGoogleBook>> = bookRepo.searchGoogleBooks(searchParams)
}
