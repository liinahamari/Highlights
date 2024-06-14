package dev.liinahamari.api.domain.usecases.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.EntryUi
import dev.liinahamari.api.domain.entities.Game
import io.reactivex.rxjava3.core.Single

interface GetGamesUseCase {
    fun getAllGames(category: Category): Single<GetAllGamesResult>
    fun filter(category: Category, countryCode: String): Single<List<Game>>
    fun findById(category: Category, id: Long): Single<Game>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Game>>
}

sealed interface GetAllGamesResult {
    object EmptyList: GetAllGamesResult
    data class Success(val data: List<EntryUi>): GetAllGamesResult
    object Error: GetAllGamesResult
}
