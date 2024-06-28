package dev.liinahamari.api.domain.usecases

import dev.liinahamari.api.domain.entities.Country

interface CacheCountriesUseCase {
    suspend fun cacheCountries()
    suspend fun getCachedCountries(): List<Country>
}
