package dev.liinahamari.impl.data.db.daos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.Entry

@Entity
data class Documentary(
    val name: String,
    override val year: Int,
    override val category: Category,
    override val posterUrl: String,
    override val countryCodes: Array<String>,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
) : Entry {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Documentary

        if (name != other.name) return false
        if (year != other.year) return false
        if (category != other.category) return false
        if (posterUrl != other.posterUrl) return false
        if (!countryCodes.contentEquals(other.countryCodes)) return false

        return true
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
        countryCodes = this.countryCodes.toList(),
        name = this.name,
        posterUrl = this.posterUrl,
        year = this.year
    )

fun dev.liinahamari.api.domain.entities.Documentary.toData(): Documentary = Documentary(
    id = this.id,
    category = this.category,
    countryCodes = this.countryCodes.toTypedArray(),
    name = this.name,
    posterUrl = this.posterUrl,
    year = this.year
)
