package dev.liinahamari.api.domain.usecases.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import io.reactivex.rxjava3.core.Single

interface GetGamesUseCase {
    fun getAllGames(category: Category): Single<List<Game>>
    fun findById(category: Category, id: Long): Single<Game>
}
