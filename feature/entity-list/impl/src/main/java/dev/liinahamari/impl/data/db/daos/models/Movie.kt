package dev.liinahamari.impl.data.db.daos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.MovieGenre
import dev.liinahamari.impl.data.db.daos.Entry

@Entity
data class Movie(
    val name: String,
    val tmdbUrl: String?,
    val genres: List<MovieGenre>,
    override val description: String,
    override val year: Int,
    override val category: Category,
    override val posterUrl: String?,
    override val countryCodes: Array<String>,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
) : Entry {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (name != other.name) return false
        if (genres != other.genres) return false
        if (year != other.year) return false
        if (category != other.category) return false
        if (tmdbUrl != other.tmdbUrl) return false
        if (posterUrl != other.posterUrl) return false
        return countryCodes.contentEquals(other.countryCodes)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + genres.hashCode()
        result = 31 * result + year
        result = 31 * result + category.hashCode()
        result = 31 * result + posterUrl.hashCode()
        result = 31 * result + tmdbUrl.hashCode()
        result = 31 * result + countryCodes.contentHashCode()
        return result
    }
}

fun Movie.toDomain(): dev.liinahamari.api.domain.entities.Movie = dev.liinahamari.api.domain.entities.Movie(
    id = this.id,
    tmdbUrl = this.tmdbUrl,
    category = this.category,
    countryCodes = this.countryCodes.toList(),
    genres = this.genres,
    name = this.name,
    posterUrl = this.posterUrl,
    year = this.year,
    description = this.description
)

fun Iterable<Movie>.toDomain(): List<dev.liinahamari.api.domain.entities.Movie> = map { it.toDomain() }

private fun dev.liinahamari.api.domain.entities.Movie.toData(): Movie = Movie(
    id = this.id,
    category = this.category,
    countryCodes = this.countryCodes.toTypedArray(),
    genres = this.genres,
    name = this.name,
    posterUrl = this.posterUrl,
    year = this.year,
    description = this.description,
    tmdbUrl = this.tmdbUrl
)

fun Array<out dev.liinahamari.api.domain.entities.Movie>.toData(): List<Movie> = map { it.toData() }
