package dev.liinahamari.impl.data.db.daos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.GameGenre
import dev.liinahamari.impl.data.db.daos.Entry

@Entity
data class Game(
    val name: String,
    val genres: List<GameGenre>,
    override val description: String,
    override val year: Int,
    override val category: Category,
    override val posterUrl: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
) : Entry {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (name != other.name) return false
        if (genres != other.genres) return false
        if (year != other.year) return false
        if (category != other.category) return false
        if (posterUrl != other.posterUrl) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + genres.hashCode()
        result = 31 * result + year
        result = 31 * result + category.hashCode()
        result = 31 * result + posterUrl.hashCode()
        return result
    }
}

fun Game.toDomain(): dev.liinahamari.api.domain.entities.Game = dev.liinahamari.api.domain.entities.Game(
    id = this.id,
    category = this.category,
    genres = this.genres,
    name = this.name,
    posterUrl = this.posterUrl,
    year = this.year,
    description = this.description
)

fun Iterable<Game>.toDomain(): List<dev.liinahamari.api.domain.entities.Game> = map { it.toDomain() }

private fun dev.liinahamari.api.domain.entities.Game.toData(): Game = Game(
    id = this.id,
    category = this.category,
    genres = this.genres,
    name = this.name,
    posterUrl = this.posterUrl,
    year = this.year,
    description = this.description
)

fun Array<out dev.liinahamari.api.domain.entities.Game>.toData(): List<Game> = map { it.toData() }
