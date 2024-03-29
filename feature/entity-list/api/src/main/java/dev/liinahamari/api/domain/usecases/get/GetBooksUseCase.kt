package dev.liinahamari.api.domain.usecases.get

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import io.reactivex.rxjava3.core.Single

interface GetBooksUseCase {
    fun getAllBooks(category: Category): Single<List<Book>>
    fun findById(category: Category, id: Long): Single<Book>
}
