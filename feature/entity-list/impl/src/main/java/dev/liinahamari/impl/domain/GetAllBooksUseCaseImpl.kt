package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.usecases.GetAllBooksUseCase
import dev.liinahamari.impl.data.repos.BooksRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetAllBooksUseCaseImpl @Inject constructor(private val booksRepo: BooksRepo): GetAllBooksUseCase {
    override fun getAllBooks(category: Category): Single<List<Book>> = booksRepo.getAllBooksByCategory(category)
}
