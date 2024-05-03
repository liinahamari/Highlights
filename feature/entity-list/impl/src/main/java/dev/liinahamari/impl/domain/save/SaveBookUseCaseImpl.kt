package dev.liinahamari.impl.domain.save

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.usecases.save.SaveBookUseCase
import dev.liinahamari.impl.data.repos.BooksRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SaveBookUseCaseImpl @Inject constructor(private val booksRepo: BooksRepo) : SaveBookUseCase {
    override fun saveBooks(vararg books: Book): Completable = booksRepo.save(*books)
}
