package dev.liinahamari.api.domain.usecases.delete

import io.reactivex.rxjava3.core.Completable

interface DeleteBookUseCase {
    fun deleteBook(id: Long): Completable
}
