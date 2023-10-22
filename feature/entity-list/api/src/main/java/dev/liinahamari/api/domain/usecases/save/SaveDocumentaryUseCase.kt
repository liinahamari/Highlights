package dev.liinahamari.api.domain.usecases.save

import dev.liinahamari.api.domain.entities.Documentary
import io.reactivex.rxjava3.core.Completable

interface SaveDocumentaryUseCase {
    fun saveDocumentary(documentary: Documentary): Completable
}
