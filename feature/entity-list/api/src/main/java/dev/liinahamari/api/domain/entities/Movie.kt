package dev.liinahamari.api.domain.entities

data class Movie(
    val id: Long,
    val category: Category,
    val year: Int,
    val posterUrl: String,
    val countryCodes: List<String>,
    val name: String,
    val genres: List<MovieGenre>,
)

enum class MovieGenre {
    ARTHOUSE,
    ACTION,
    THRILLER,
    HORROR,
    DRAMA,
    COMEDY,
    WESTERN,
    SCIENCE_FICTION,
    ADVENTURE,
    HISTORY,
    FANTASY,
    HISTORICAL_FICTION,
    DETECTIVE_FICTION,
    MAGICAL_REALISM,
    POST_APOCALYPTIC_FICTION
}

