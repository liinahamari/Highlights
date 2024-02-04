package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.usecases.CloseDbEvent
import dev.liinahamari.api.domain.usecases.CloseDbUseCase
import dev.liinahamari.impl.data.db.EntriesDatabase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class CloseDbUseCaseImpl @Inject constructor(private val db: EntriesDatabase) :
    CloseDbUseCase {
    override fun closeDb(): Single<CloseDbEvent> = Completable.fromAction { db.close() }
        .andThen<CloseDbEvent>(Single.just(CloseDbEvent.Success))
        .onErrorReturn { CloseDbEvent.Error }
}
