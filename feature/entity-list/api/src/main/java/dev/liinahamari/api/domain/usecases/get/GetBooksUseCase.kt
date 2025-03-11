package dev.liinahamari.api.domain.usecases.get

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.BookUi
import dev.liinahamari.api.domain.entities.Category
import io.reactivex.rxjava3.core.Single

interface GetBooksUseCase {
    fun getAllBooks(category: Category): Single<GetAllBooksResult>
    fun filter(category: Category, countryCode: String): Single<List<Book>>
    fun findById(category: Category, id: Long): Single<Book>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Book>>
}

sealed interface GetAllBooksResult {
    data object EmptyList : GetAllBooksResult
    data class Success(val data: List<BookUi>) : GetAllBooksResult
    data object Error : GetAllBooksResult
}
