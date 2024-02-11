package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.MovieDao
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DatabaseCountersUseCaseImpl @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val gameDao: GameDao,
    private val documentariesDao: DocumentaryDao
) : DatabaseCountersUseCase {
    override fun getTotalAmount(): Single<Int> =
        Single.zip(
            bookDao.getRowCount(),
            movieDao.getRowCount(),
            gameDao.getRowCount(),
            documentariesDao.getRowCount()
        ) { t1, t2, t3, t4 -> t1 + t2 + t3 + t4 }

    override fun getMoviesAmount(): Single<Int> = movieDao.getRowCount()

    override fun getDocumentariesAmount(): Single<Int> = documentariesDao.getRowCount()

    override fun getBooksAmount(): Single<Int> = bookDao.getRowCount()

    override fun getGamesAmount(): Single<Int> = gameDao.getRowCount()
}
