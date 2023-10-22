package dev.liinahamari.api.domain.usecases.delete

import io.reactivex.rxjava3.core.Completable

interface DeleteDocumentaryUseCase {
    fun deleteDocumentary(id: Long): Completable
}
