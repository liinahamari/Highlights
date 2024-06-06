package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.entities.DatabaseCounters
import dev.liinahamari.api.domain.entities.Entity
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.MovieDao
import javax.inject.Inject

class DatabaseCountersUseCaseImpl @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val gameDao: GameDao,
    private val documentariesDao: DocumentaryDao
) : DatabaseCountersUseCase {
    override suspend fun getAllDatabaseCounters(): DatabaseCounters = listOf(
        getGamesAmount(),
        getMoviesAmount(),
        getBooksAmount(),
        getDocumentariesAmount()
    )
        .let { entities ->
            val allEntitiesCounter = entities.sumOf { it.counter }
            when (allEntitiesCounter) {
                0 -> DatabaseCounters.Empty
                in 0..Int.MAX_VALUE -> DatabaseCounters.Success(
                    entities = entities,
                    totalCounter = allEntitiesCounter.toString(),
                    titleInCenterOfChart = "Total ($allEntitiesCounter)"
                )

                else -> DatabaseCounters.DatabaseCorruptionError
            }
        }

    private suspend fun getMoviesAmount(): Entity = movieDao.getRowCount().let(Entity::Movies)
    private suspend fun getDocumentariesAmount(): Entity = documentariesDao.getRowCount().let(Entity::Documentaries)
    private suspend fun getBooksAmount(): Entity = bookDao.getRowCount().let(Entity::Books)
    private suspend fun getGamesAmount(): Entity = gameDao.getRowCount().let(Entity::Games)
}
