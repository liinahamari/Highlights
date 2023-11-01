package dev.liinahamari.suggestions.impl.usecases

import dev.liinahamari.suggestions.api.usecases.SearchGameUseCase
import dev.liinahamari.suggestions.api.model.RemoteGame
import dev.liinahamari.suggestions.impl.data.repos.GameRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchGameUseCaseImpl @Inject constructor(private val gameRepo: GameRepo) : SearchGameUseCase {
    override fun search(searchParams: String): Single<List<RemoteGame>> = gameRepo.search(searchParams)
}
