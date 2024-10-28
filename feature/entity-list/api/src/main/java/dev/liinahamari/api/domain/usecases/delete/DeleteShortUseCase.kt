package dev.liinahamari.api.domain.usecases.delete

import io.reactivex.rxjava3.core.Completable

interface DeleteShortUseCase {
    fun deleteShort(id: Long): Completable
}
