package dev.liinahamari.api.domain.usecases.delete

import io.reactivex.rxjava3.core.Completable

interface DeleteGameUseCase {
    fun deleteGame(id: Long): Completable
}
