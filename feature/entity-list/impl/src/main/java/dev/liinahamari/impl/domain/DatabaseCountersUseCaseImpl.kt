package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.entities.DatabaseCounters
import dev.liinahamari.api.domain.entities.Entity
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.MovieDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DatabaseCountersUseCaseImpl @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val gameDao: GameDao,
    private val documentariesDao: DocumentaryDao
) : DatabaseCountersUseCase {
    override fun getAllDatabaseCounters(): Single<DatabaseCounters> = Maybe.concat(
        getGamesAmount(),
        getMoviesAmount(),
        getBooksAmount(),
        getDocumentariesAmount()
    ).toList()
        .map { counters ->
            if (counters.isEmpty()) {
                DatabaseCounters.Empty
            } else {
                val sum = counters.map { it.counter }.sum()
                DatabaseCounters.Success(
                    entities = counters,
                    totalCounter = sum.toString(),
                    titleInCenterOfChart = "Total ($sum)"
                )
            }
        }

    private fun getMoviesAmount(): Maybe<Entity> = movieDao.getRowCount()
        .filter { it > 0 }
        .map<Entity> { Entity.Movies(it.toFloat()) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    private fun getDocumentariesAmount(): Maybe<Entity> = documentariesDao.getRowCount()
        .filter { it > 0 }
        .map<Entity> { Entity.Documentaries(it.toFloat()) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    private fun getBooksAmount(): Maybe<Entity> = bookDao.getRowCount()
        .filter { it > 0 }
        .map<Entity> { Entity.Books(it.toFloat()) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    private fun getGamesAmount(): Maybe<Entity> = gameDao.getRowCount()
        .filter { it > 0 }
        .map<Entity> { Entity.Games(it.toFloat()) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
