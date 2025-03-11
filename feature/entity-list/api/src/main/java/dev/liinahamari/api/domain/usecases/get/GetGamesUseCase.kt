package dev.liinahamari.api.domain.usecases.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.GameUi
import io.reactivex.rxjava3.core.Single

interface GetGamesUseCase {
    fun getAllGames(category: Category): Single<GetAllGamesResult>
    fun findById(category: Category, id: Long): Single<Game>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Game>>
}

sealed interface GetAllGamesResult {
    data object EmptyList : GetAllGamesResult
    data class Success(val data: List<GameUi>) : GetAllGamesResult
    data object Error : GetAllGamesResult
}
