package dev.liinahamari.api.domain.usecases.save

import dev.liinahamari.api.domain.entities.Book
import io.reactivex.rxjava3.core.Completable

interface SaveBookUseCase {
    fun saveBook(book: Book): Completable
}
