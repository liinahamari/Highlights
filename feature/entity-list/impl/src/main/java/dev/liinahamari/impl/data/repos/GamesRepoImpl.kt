package dev.liinahamari.impl.data.repos

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.models.toData
import dev.liinahamari.impl.data.db.daos.models.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GamesRepoImpl @Inject constructor(private val gamesDao: GameDao) : GamesRepo {
    override fun getAllGamesByCategory(category: Category): Single<List<Game>> =
        gamesDao.getAll(category)
            .toObservable()
            .map { it.toDomain() }
            .firstOrError()

    override fun delete(id: Long): Completable = gamesDao.delete(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun save(vararg games: Game): Completable = gamesDao.insert(games.toData())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun findById(category: Category, id: Long): Single<Game> = gamesDao.findById(category, id)
        .map { it.toDomain() }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun findByIds(category: Category, ids: Set<Long>): Single<List<Game>> = gamesDao.findByIds(category, ids)
        .map { it.toDomain() }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

interface GamesRepo {
    fun getAllGamesByCategory(category: Category): Single<List<Game>>
    fun save(vararg games: Game): Completable
    fun delete(id: Long): Completable
    fun findById(category: Category, id: Long): Single<Game>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Game>>
}
