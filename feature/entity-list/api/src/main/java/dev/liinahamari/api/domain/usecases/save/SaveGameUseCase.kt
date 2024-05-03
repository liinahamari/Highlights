package dev.liinahamari.api.domain.usecases.save

import dev.liinahamari.api.domain.entities.Game
import io.reactivex.rxjava3.core.Completable

interface SaveGameUseCase {
    fun saveGames(vararg games: Game): Completable
}
