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
    override suspend fun getAllDatabaseCounters(filterActual: Boolean): DatabaseCounters = withContext(Dispatchers.IO) {
        listOf(
            getGamesAmount(filterActual),
            getMoviesAmount(filterActual),
            getBooksAmount(filterActual),
            getDocumentariesAmount(filterActual),
            getShortsAmount(filterActual)
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

    private suspend fun getMoviesAmount(filterActual: Boolean = false): Entity = if (filterActual)
        movieDao.getRowActualCount().let(Entity::Movies)
    else
        movieDao.getRowCount().let(Entity::Movies)

    private suspend fun getDocumentariesAmount(filterActual: Boolean = false): Entity = if (filterActual)
        documentariesDao.getRowActualCount().let(Entity::Documentaries)
    else
        documentariesDao.getRowCount().let(Entity::Documentaries)

    private suspend fun getShortsAmount(filterActual: Boolean = false): Entity = if (filterActual)
        shortsDao.getRowActualCount().let(Entity::Shorts)
    else
        shortsDao.getRowCount().let(Entity::Shorts)

    private suspend fun getBooksAmount(filterActual: Boolean = false): Entity = if (filterActual)
        bookDao.getRowActualCount().let(Entity::Books)
    else
        bookDao.getRowCount().let(Entity::Books)

    private suspend fun getGamesAmount(filterActual: Boolean = false): Entity = if (filterActual)
        gameDao.getRowActualCount().let(Entity::Games)
    else
        gameDao.getRowCount().let(Entity::Games)
}
