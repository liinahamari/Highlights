package dev.liinahamari.api.domain.entities

import java.time.Year

data class BookUi(
    val id: Long,
    val title: String,
    val author: String?,
    val productionCountries: List<CountryUi>,
    val genres: List<BookGenreUi>,
    val firstPublicationYear: Year?,
    val description: String?,
    val coverUrl: String?,
    val isImported: Boolean
)

data class MovieUi(
    val id: Long,
    val title: String,
    val directorsNames: List<String>,
    val actorsNames: List<String>,
    val productionCounties: List<CountryUi>,
    val genres: List<MovieGenreUi>,
    val releaseYear: Year?,
    val description: String?,
    val posterUrl: String?,
    val isImported: Boolean,
    val tmdbUrl: String?
)

data class ShortUi(
    val id: Long,
    val title: String,
    val directorsNames: List<String>,
    val actorsNames: List<String>,
    val productionCounties: List<CountryUi>,
    val genres: List<MovieGenreUi>,
    val releaseYear: Year?,
    val description: String?,
    val posterUrl: String?,
    val isImported: Boolean,
    val tmdbUrl: String?
)

data class GameUi(
    val id: Long,
    val title: String,
    val genres: List<GameGenreUi>,
    val year: Year,
    val description: String?,
    val posterUrl: String?,
    val isImported: Boolean
)

data class DocumentaryUi(
    val id: Long,
    val title: String,
    val productionCounties: List<CountryUi>,
    val releaseYear: Year?,
    val description: String?,
    val posterUrl: String?,
    val isImported: Boolean,
    val tmdbUrl: String?
)

typealias CountryUi = String
typealias GameGenreUi = String
typealias MovieGenreUi = String
typealias BookGenreUi = String
