package dev.liinahamari.impl.domain.delete

import dev.liinahamari.api.domain.usecases.delete.DeleteBookUseCase
import dev.liinahamari.impl.data.repos.BooksRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteBookUseCaseImpl @Inject constructor(private val booksRepo: BooksRepo) : DeleteBookUseCase {
    override fun deleteBook(id: Long): Completable = booksRepo.delete(id)
}
