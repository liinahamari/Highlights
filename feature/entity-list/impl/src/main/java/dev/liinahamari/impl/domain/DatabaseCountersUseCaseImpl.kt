package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.usecases.DatabaseCounters
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.api.domain.usecases.Entity
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.MovieDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DatabaseCountersUseCaseImpl @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val gameDao: GameDao,
    private val documentariesDao: DocumentaryDao
) : DatabaseCountersUseCase {
    override fun getAllDatabaseCounters(): Single<DatabaseCounters> = Single.zip(
        getGamesAmount(),
        getMoviesAmount(),
        getBooksAmount(),
        getDocumentariesAmount()
    ) { gamesCounter, moviesCounter, booksCounter, documentariesCounter ->
        DatabaseCounters(
            entities = listOf(
                Entity.Games(gamesCounter.toFloat()), Entity.Documentaries(documentariesCounter.toFloat()),
                Entity.Movies(moviesCounter.toFloat()),
                Entity.Books(booksCounter.toFloat())
            ), totalCounter = (gamesCounter + moviesCounter + booksCounter + documentariesCounter).toString()
        )
    }

    private fun getMoviesAmount(): Single<Int> = movieDao.getRowCount()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    private fun getDocumentariesAmount(): Single<Int> = documentariesDao.getRowCount()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    private fun getBooksAmount(): Single<Int> = bookDao.getRowCount()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    private fun getGamesAmount(): Single<Int> = gameDao.getRowCount()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
