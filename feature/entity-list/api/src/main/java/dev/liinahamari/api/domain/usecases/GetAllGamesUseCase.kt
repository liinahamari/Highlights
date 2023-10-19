package dev.liinahamari.api.domain.usecases

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import io.reactivex.rxjava3.core.Single

interface GetAllGamesUseCase {
    fun getAllGames(category: Category): Single<List<Game>>
}
