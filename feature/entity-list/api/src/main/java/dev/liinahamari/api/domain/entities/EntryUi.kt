package dev.liinahamari.api.domain.entities

data class EntryUi(
    val id: Long,
    val title: String,
    val countries: List<String>,
    val genres: String,
    val year: Int,
    val description: String,
    val posterUrl: String?,
    val clazz: Class<*>,
    val tmdbUrl: String?
)
data class BookUi(
    val id: Long,
    val title: String,
    val author: String,
    val countries: List<CountryUi>,
    val genres: List<BookGenreUi>,
    val year: Int,
    val description: String?,
    val coverUrl: String?,
)

data class MovieUi(
    val id: Long,
    val title: String,
    val directorsName: String,
    val countries: List<CountryUi>,
    val genres: List<MovieGenreUi>,
    val year: Int,
    val description: String?,
    val posterUrl: String?,
)

data class GameUi(
    val id: Long,
    val title: String,
    val genres: List<GameGenreUi>,
    val year: Int,
    val description: String?,
    val posterUrl: String?,
)

data class DocumentaryUi(
    val id: Long,
    val title: String,
    val countries: List<CountryUi>,
    val year: Int,
    val description: String?,
    val posterUrl: String?,
)

typealias CountryUi = String
typealias GameGenreUi = String
typealias MovieGenreUi = String
typealias BookGenreUi = String
