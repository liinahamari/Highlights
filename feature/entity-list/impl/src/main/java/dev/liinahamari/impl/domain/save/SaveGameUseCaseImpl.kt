package dev.liinahamari.impl.domain.save

import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.usecases.save.SaveGameUseCase
import dev.liinahamari.impl.data.repos.GamesRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SaveGameUseCaseImpl @Inject constructor(private val gamesRepo: GamesRepo) : SaveGameUseCase {
    override fun saveGames(vararg games: Game): Completable = gamesRepo.save(*games)
}
