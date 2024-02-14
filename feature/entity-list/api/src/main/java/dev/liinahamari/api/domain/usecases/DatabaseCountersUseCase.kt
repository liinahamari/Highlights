package dev.liinahamari.api.domain.usecases

import dev.liinahamari.api.domain.entities.DatabaseCounters
import io.reactivex.rxjava3.core.Single

interface DatabaseCountersUseCase {
    fun getAllDatabaseCounters(): Single<DatabaseCounters>
}
