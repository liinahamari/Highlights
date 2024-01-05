package dev.liinahamari.api.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize //fixme domain model in suggestions-ui module
data class Movie(
    val id: Long,
    val category: Category,
    val year: Int,
    val posterUrl: String?,
    val countryCodes: List<String>,
    val name: String,
    val genres: List<MovieGenre>,
): Parcelable {
    companion object{
        fun default(category: Category) = Movie(
            0L, category, 0, "", listOf(), "", listOf()
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

