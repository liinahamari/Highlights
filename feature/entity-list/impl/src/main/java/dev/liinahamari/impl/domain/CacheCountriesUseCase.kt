package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.usecases.CacheCountriesUseCase
import dev.liinahamari.impl.data.db.CachedCountriesDao
import dev.liinahamari.impl.data.db.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class CacheCountriesUseCaseImpl @Inject constructor(private val cachedCountriesDao: CachedCountriesDao) :
    CacheCountriesUseCase {
    override suspend fun cacheCountries() {
        withContext(Dispatchers.IO) {
            if (cachedCountriesDao.getRowCount() == 0) {
                Locale.getISOCountries()
                    .map { Locale("", it) }
                    .filter { it.country.isNullOrBlank().not() && it.displayCountry.isNullOrBlank().not() }
                    .map { Country(it.country, it.getDisplayCountry(Locale.US)) /*todo: multi-language support*/}
                    .sortedBy { it.countryEnglishName }
                    .apply { cachedCountriesDao.insertAll(this) }
            }
        }
    }

    @Volatile
    private var cachedCountries = emptyList<dev.liinahamari.api.domain.entities.Country>()

    override suspend fun getCachedCountries(): List<dev.liinahamari.api.domain.entities.Country> =
        cachedCountries.ifEmpty {
            withContext(Dispatchers.IO) {
                cachedCountriesDao.getAll()
                    .map {
                        dev.liinahamari.api.domain.entities.Country(
                            name = it.countryEnglishName,
                            iso = it.isoCountryCode
                        )
                    }
                    .also { cachedCountries = it }
            }
        }.ifEmpty {
            cacheCountries()
            getCachedCountries()
        }
}
