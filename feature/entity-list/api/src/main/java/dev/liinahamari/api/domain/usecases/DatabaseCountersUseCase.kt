package dev.liinahamari.api.domain.usecases

import dev.liinahamari.api.domain.entities.DatabaseCounters

interface DatabaseCountersUseCase {
    suspend fun getAllDatabaseCounters(filterActual: Boolean = false): DatabaseCounters
}
