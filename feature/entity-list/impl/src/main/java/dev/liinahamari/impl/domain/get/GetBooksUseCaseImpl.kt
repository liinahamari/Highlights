package dev.liinahamari.impl.domain.get

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.impl.data.repos.BooksRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetBooksUseCaseImpl @Inject constructor(private val booksRepo: BooksRepo): GetBooksUseCase {
    override fun getAllBooks(category: Category): Single<List<Book>> = booksRepo.getAllBooksByCategory(category)
    override fun findById(category: Category, id: Long): Single<Book> = booksRepo.findById(category, id)
    override fun findByIds(category: Category, ids: Set<Long>): Single<List<Book>> = booksRepo.findByIds(category, ids)
}
