package dev.liinahamari.api.domain.usecases

import io.reactivex.rxjava3.core.Single

interface CloseDbUseCase {
    fun closeDb(): Single<CloseDbEvent>
}

sealed interface CloseDbEvent {
    object Success: CloseDbEvent
    object Error: CloseDbEvent
}
