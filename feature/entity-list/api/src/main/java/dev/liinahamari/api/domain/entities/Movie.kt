package dev.liinahamari.api.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize //fixme domain model in suggestions-ui module
data class Movie(
    val localId: Long = 0L,
    val category: Category,
    val tmdbId: Int,
    val tmdbUrl: String?,
    val releaseYear: Int,
    val posterUrl: String?,
    val productionCountries: List<Country>,
    val title: String,
    val genres: List<MovieGenre>,
    val description: String
) : Parcelable {
    companion object {
        fun default(category: Category = Category.GOOD) = Movie(
            0L, category, 0, "", 0, "", listOf(), "", listOf(), ""
        )
    }
}

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
    CRIME,
    MUSIC,
    DETECTIVE_FICTION,
    MAGICAL_REALISM,
    POST_APOCALYPTIC_FICTION
}

