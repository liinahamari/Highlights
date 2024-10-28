package dev.liinahamari.impl.data.db.daos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Country
import dev.liinahamari.impl.data.db.daos.Entry

@Entity
data class Short(
    val name: String,
    val tmdbUrl: String?,
    val tmdbId: Int,
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

        other as Movie

        if (name != other.name) return false
        if (year != other.year) return false
        if (category != other.category) return false
        if (tmdbUrl != other.tmdbUrl) return false
        if (posterUrl != other.posterUrl) return false
        return countryCodes.contentEquals(other.countryCodes)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + year
        result = 31 * result + category.hashCode()
        result = 31 * result + posterUrl.hashCode()
        result = 31 * result + tmdbUrl.hashCode()
        result = 31 * result + countryCodes.contentHashCode()
        return result
    }
}

fun Short.toDomain(): dev.liinahamari.api.domain.entities.Short = dev.liinahamari.api.domain.entities.Short(
    localId = this.id,
    tmdbUrl = this.tmdbUrl,
    category = this.category,
    productionCountries = this.countryCodes.toList().map { Country(iso = it, name = "") },
    title = this.name,
    posterUrl = this.posterUrl,
    releaseYear = this.year,
    description = this.description,
    tmdbId = this.tmdbId
)

fun Iterable<Short>.toDomain(): List<dev.liinahamari.api.domain.entities.Short> = map { it.toDomain() }

private fun dev.liinahamari.api.domain.entities.Short.toData(): Short = Short(
    id = this.localId,
    category = this.category,
    countryCodes = this.productionCountries.map { it.iso }.toTypedArray(),
    name = this.title,
    posterUrl = this.posterUrl,
    year = this.releaseYear,
    description = this.description,
    tmdbUrl = this.tmdbUrl,
    tmdbId = this.tmdbId
)

fun Array<out dev.liinahamari.api.domain.entities.Short>.toData(): List<Short> = map { it.toData() }
