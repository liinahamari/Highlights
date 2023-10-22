package dev.liinahamari.impl.domain.delete

import dev.liinahamari.api.domain.usecases.delete.DeleteGameUseCase
import dev.liinahamari.impl.data.repos.GamesRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteGameUseCaseImpl @Inject constructor(private val gamesRepo: GamesRepo) : DeleteGameUseCase {
    override fun deleteGame(id: Long): Completable = gamesRepo.delete(id)
}
