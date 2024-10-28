package dev.liinahamari.impl.data.db.daos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Country
import dev.liinahamari.impl.data.db.daos.Entry

@Entity
data class Documentary(
    val name: String,
    val tmdbUrl: String?,
    val tmdbId: Int?,
    override val description: String,
    override val year: Int,
    override val category: Category,
    override val posterUrl: String?,
    val countryCodes: Array<String>,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
) : Entry {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Documentary

        if (name != other.name) return false
        if (year != other.year) return false
        if (category != other.category) return false
        if (posterUrl != other.posterUrl) return false
        return countryCodes.contentEquals(other.countryCodes)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + year
        result = 31 * result + category.hashCode()
        result = 31 * result + posterUrl.hashCode()
        result = 31 * result + countryCodes.contentHashCode()
        return result
    }
}

fun Documentary.toDomain(): dev.liinahamari.api.domain.entities.Documentary =
    dev.liinahamari.api.domain.entities.Documentary(
        id = this.id,
        category = this.category,
        countryCodes = this.countryCodes.toList().map { Country(iso = it, name = "")/*fixme: actual*/ },
        name = this.name,
        posterUrl = this.posterUrl,
        year = this.year,
        description = this.description,
        tmdbUrl = tmdbUrl,
        tmdbId = this.tmdbId
    )

fun Iterable<Documentary>.toDomain(): List<dev.liinahamari.api.domain.entities.Documentary> = map { it.toDomain() }

private fun dev.liinahamari.api.domain.entities.Documentary.toData(): Documentary = Documentary(
    id = this.id,
    category = this.category,
    countryCodes = this.countryCodes.map { it.iso }.toTypedArray(),
    name = this.name,
    posterUrl = this.posterUrl,
    year = this.year,
    description = this.description,
    tmdbUrl = this.tmdbUrl,
    tmdbId = this.tmdbId
)

fun Array<out dev.liinahamari.api.domain.entities.Documentary>.toData(): List<Documentary> = map { it.toData() }
