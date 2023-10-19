package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.usecases.GetAllGamesUseCase
import dev.liinahamari.impl.data.repos.GamesRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetAllGamesUseCaseImpl @Inject constructor(private val gamesRepo: GamesRepo) : GetAllGamesUseCase {
    override fun getAllGames(category: Category): Single<List<Game>> = gamesRepo.getAllGamesByCategory(category)
}
