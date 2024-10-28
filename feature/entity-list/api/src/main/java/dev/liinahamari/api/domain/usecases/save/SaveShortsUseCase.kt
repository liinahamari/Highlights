package dev.liinahamari.api.domain.usecases.save

import dev.liinahamari.api.domain.entities.Short
import io.reactivex.rxjava3.core.Completable

interface SaveShortsUseCase {
    fun saveShorts(vararg shorts: Short): Completable
}
