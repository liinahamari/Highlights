package dev.liinahamari.api.domain.entities

import java.time.Year
import java.util.Locale

fun Documentary.toUi() = DocumentaryUi(
    id = id,
    title = name,
    description = description,
    productionCounties = countryCodes.toCountriesNames(),
    posterUrl = posterUrl,
    releaseYear = Year.parse(year.toString()),
    isImported = false/*fixme*/,
    tmdbUrl = tmdbUrl
)

fun List<Documentary>.toDocumentaryUi() = map { it.toUi() }

fun Book.toUi() = BookUi(
    id = id,
    title = name,
    description = description,
    genres = genres.map { it.name },
    productionCountries = countries.toCountriesNames(),
    coverUrl = posterUrl,
    firstPublicationYear = Year.parse(year.toString()),
    isImported = false/*fixme*/,
    author = author
)

fun List<Book>.toBookUi() = map { it.toUi() }

fun Game.toUi() = GameUi(
    id,
    title = name,
    description = description,
    genres = genres.map { it.name },
    posterUrl = posterUrl,
    year = Year.parse(year.toString()),
    isImported = false/*fixme*/
)

fun List<Game>.toGameUi() = map { it.toUi() }

fun Movie.toUi() = MovieUi(
    localId,
    title = title,
    description = description,
    genres = genres.map { it.name },
    productionCounties = productionCountries.toCountriesNames(),
    posterUrl = posterUrl,
    releaseYear = Year.parse(releaseYear.toString()),
    tmdbUrl = tmdbUrl,
    isImported = false/*fixme*/,
    directorsNames = listOf()/*fixme*/,
    actorsNames = listOf()/*fixme*/
)

fun List<Movie>.toMovieUi() = map { it.toUi() }

fun Short.toUi() = ShortUi(
    localId,
    title = title,
    description = description,
    genres = genres.map { it.name },
    productionCounties = productionCountries.toCountriesNames(),
    posterUrl = posterUrl,
    releaseYear = Year.parse(releaseYear.toString()),
    tmdbUrl = tmdbUrl,
    isImported = false/*fixme*/,
    actorsNames = listOf()/*fixme*/,
    directorsNames = listOf()/*fixme*/
)

fun List<Short>.toShortUi() = map { it.toUi() }

private fun List<Country>.toCountriesNames() =
    map { it.name.ifBlank { Locale("", it.iso).getDisplayCountry(Locale.US) } }
