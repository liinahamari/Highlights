package dev.liinahamari.impl.data.repos

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GamesRepoImpl @Inject constructor(private val gamesDao: GameDao) : GamesRepo {
    override fun getAllGamesByCategory(category: Category): Single<List<Game>> =
        gamesDao.getAll(category)
            .toObservable()
            .map { it.map { it.toDomain() } }
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}

interface GamesRepo {
    fun getAllGamesByCategory(category: Category): Single<List<Game>>
}
