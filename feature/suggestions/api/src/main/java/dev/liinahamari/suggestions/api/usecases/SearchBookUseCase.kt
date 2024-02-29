package dev.liinahamari.suggestions.api.usecases

import dev.liinahamari.suggestions.api.model.books.RemoteGoogleBook
import dev.liinahamari.suggestions.api.model.books.RemoteOpenLibraryBook
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface SearchBookUseCase {
    fun searchOpenLibrary(searchParams: String): Single<List<RemoteOpenLibraryBook>>
    fun searchGoogleBooks(searchParams: String): Maybe<List<RemoteGoogleBook>>
}
