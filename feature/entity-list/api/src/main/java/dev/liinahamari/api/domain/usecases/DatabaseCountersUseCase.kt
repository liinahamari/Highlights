package dev.liinahamari.api.domain.usecases

import io.reactivex.rxjava3.core.Single

interface DatabaseCountersUseCase {
    fun getAllDatabaseCounters(): Single<DatabaseCounters>
}

data class DatabaseCounters(
    val entities: List<Entity>,
    val totalCounter: String
)

sealed interface Entity {
    val counter: Float
    data class Games(override val counter: Float): Entity
    data class Documentaries(override val counter: Float): Entity
    data class Movies(override val counter: Float): Entity
    data class Books(override val counter: Float): Entity
}
