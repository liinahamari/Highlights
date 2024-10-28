package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.entities.DatabaseCounters
import dev.liinahamari.api.domain.entities.Entity
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.MovieDao
import dev.liinahamari.impl.data.db.daos.ShortsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseCountersUseCaseImpl @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val gameDao: GameDao,
    private val documentariesDao: DocumentaryDao,
    private val shortsDao: ShortsDao
) : DatabaseCountersUseCase {
    override suspend fun getAllDatabaseCounters(): DatabaseCounters = withContext(Dispatchers.IO) {
        listOf(
            getGamesAmount(),
            getMoviesAmount(),
            getBooksAmount(),
            getDocumentariesAmount(),
            getShortsAmount()
        )
            .let { entities ->
                when (val allEntitiesCounter = entities.sumOf { it.counter }) {
                    0 -> DatabaseCounters.Empty
                    in 0..Int.MAX_VALUE -> DatabaseCounters.Success(
                        entities = entities,
                        totalCounter = allEntitiesCounter.toString(),
                        titleInCenterOfChart = "Total ($allEntitiesCounter)"
                    )

                    else -> throw IllegalStateException("Sum of entities in database equal to $allEntitiesCounter")
                }
            }
    }

    private suspend fun getMoviesAmount(): Entity = movieDao.getRowCount().let(Entity::Movies)
    private suspend fun getDocumentariesAmount(): Entity = documentariesDao.getRowCount().let(Entity::Documentaries)
    private suspend fun getShortsAmount(): Entity = shortsDao.getRowCount().let(Entity::Shorts)
    private suspend fun getBooksAmount(): Entity = bookDao.getRowCount().let(Entity::Books)
    private suspend fun getGamesAmount(): Entity = gameDao.getRowCount().let(Entity::Games)
}
