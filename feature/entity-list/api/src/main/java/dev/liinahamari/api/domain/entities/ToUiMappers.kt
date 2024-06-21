package dev.liinahamari.api.domain.entities

import java.util.Locale

fun Documentary.toUi() = EntryUi(
    id = id,
    title = name,
    description = description,
    genres = "",
    countries = countryCodes.map { Locale("", it).displayCountry },
    posterUrl = posterUrl,
    year = year,
    clazz = Documentary::class.java,
    tmdbUrl = tmdbUrl
)

fun List<Documentary>.toDocumentaryUi() = map { it.toUi() }
fun Book.toUi() = EntryUi(
    id = id,
    title = name,
    description = description,
    genres = "",
    countries = countryCodes.map { Locale("", it).displayCountry },
    posterUrl = posterUrl,
    year = year,
    clazz = Book::class.java,
            tmdbUrl = null
)

fun List<Book>.toBookUi() = map { it.toUi() }

fun Game.toUi() = EntryUi(
    id,
    title = name,
    description = description,
    genres = "",
    countries = countryCodes.map { Locale("", it).displayCountry },
    posterUrl = posterUrl,
    year = year,
    clazz = Game::class.java,
    tmdbUrl = null
)

fun List<Game>.toGameUi() = map { it.toUi() }

fun Movie.toUi() = EntryUi(
    id,
    title = name,
    description = description,
    genres = "",
    countries = countryCodes.map { Locale("", it).displayCountry },
    posterUrl = posterUrl,
    year = year,
    clazz = Movie::class.java,
    tmdbUrl = tmdbUrl
)


fun List<Movie>.toMovieUi() = map { it.toUi() }
