package dev.liinahamari.impl.data.repos

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Short
import dev.liinahamari.impl.data.db.daos.ShortsDao
import dev.liinahamari.impl.data.db.daos.models.toData
import dev.liinahamari.impl.data.db.daos.models.toDomain
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ShortsRepoImpl @Inject constructor(private val shortsDao: ShortsDao) : ShortsRepo {
    override fun getAllShortsByCategory(category: Category): Single<List<Short>> = shortsDao.getAll(category)
        .toObservable()
        .map { it.toDomain() }
        .firstOrError()

    override fun delete(id: Long): Completable = shortsDao.delete(id)

    override fun filterByCountry(category: Category, countryCode: String): Single<List<Short>> =
        shortsDao.filterByCountry(category, countryCode)
            .map { it.toDomain() }

    override fun save(vararg shorts: Short): Completable = shortsDao.insert(shorts.toData())
    override fun findById(category: Category, id: Long): Single<Short> = shortsDao.findById(category, id)
        .map { it.toDomain() }

    override fun findByIds(category: Category, ids: Set<Long>): Single<List<Short>> = shortsDao.findByIds(category, ids)
        .map { it.toDomain() }
}

interface ShortsRepo {
    fun getAllShortsByCategory(category: Category): Single<List<Short>>
    fun save(vararg shorts: Short): Completable
    fun delete(id: Long): Completable
    fun findById(category: Category, id: Long): Single<Short>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Short>>
    fun filterByCountry(category: Category, countryCode: String): Single<List<Short>>
}
