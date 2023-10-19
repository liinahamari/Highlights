package dev.liinahamari.api.domain.usecases

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import io.reactivex.rxjava3.core.Single

interface GetAllBooksUseCase {
    fun getAllBooks(category: Category): Single<List<Book>>
}
