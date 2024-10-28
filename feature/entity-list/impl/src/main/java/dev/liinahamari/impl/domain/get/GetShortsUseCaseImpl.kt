package dev.liinahamari.impl.domain.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Short
import dev.liinahamari.api.domain.entities.toShortUi
import dev.liinahamari.api.domain.usecases.get.GetAllShortsResult
import dev.liinahamari.api.domain.usecases.get.GetShortsUseCase
import dev.liinahamari.impl.data.repos.ShortsRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetShortsUseCaseImpl @Inject constructor(private val shortsRepo: ShortsRepo) : GetShortsUseCase {
    override fun filter(category: Category, countryCode: String): Single<List<Short>> =
        shortsRepo.filterByCountry(category, countryCode)

    override fun getAllShorts(category: Category): Single<GetAllShortsResult> =
        shortsRepo.getAllShortsByCategory(category)
            .map {
                if (it.isEmpty().not()) {
                    GetAllShortsResult.Success(it.toShortUi())
                } else {
                    GetAllShortsResult.EmptyList
                }
            }

    override fun findById(category: Category, id: Long): Single<Short> = shortsRepo.findById(category, id)
    override fun findByIds(category: Category, ids: Set<Long>): Single<List<Short>> =
        shortsRepo.findByIds(category, ids)
}
