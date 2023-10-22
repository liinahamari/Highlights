package dev.liinahamari.impl.domain.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.impl.data.repos.GamesRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetGamesUseCaseImpl @Inject constructor(private val gamesRepo: GamesRepo) : GetGamesUseCase {
    override fun getAllGames(category: Category): Single<List<Game>> = gamesRepo.getAllGamesByCategory(category)
    override fun findById(category: Category, id: Long): Single<Game> = gamesRepo.findById(category, id)
}
