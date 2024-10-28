package dev.liinahamari.api.domain.usecases.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.EntryUi
import dev.liinahamari.api.domain.entities.Short
import io.reactivex.rxjava3.core.Single

interface GetShortsUseCase {
    fun getAllShorts(category: Category): Single<GetAllShortsResult>
    fun filter(category: Category, countryCode: String): Single<List<Short>>
    fun findById(category: Category, id: Long): Single<Short>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Short>>
}

sealed interface GetAllShortsResult {
    data object EmptyList : GetAllShortsResult
    data class Success(val data: List<EntryUi>) : GetAllShortsResult
    data object Error : GetAllShortsResult
}
