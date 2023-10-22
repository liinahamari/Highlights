package dev.liinahamari.impl.data.db.daos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.liinahamari.api.domain.entities.BookGenre
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.Entry

@Entity
data class Book(
    val name: String,
    val genres: List<BookGenre>,
    val author: String,
    override val year: Int,
    override val category: Category,
    override val posterUrl: String,
    override val countryCodes: Array<String>,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
) : Entry {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (name != other.name) return false
        if (genres != other.genres) return false
        if (author != other.author) return false
        if (year != other.year) return false
        if (category != other.category) return false
        if (posterUrl != other.posterUrl) return false
        if (!countryCodes.contentEquals(other.countryCodes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + genres.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + year
        result = 31 * result + category.hashCode()
        result = 31 * result + posterUrl.hashCode()
        result = 31 * result + countryCodes.contentHashCode()
        return result
    }
}

fun Book.toDomain(): dev.liinahamari.api.domain.entities.Book = dev.liinahamari.api.domain.entities.Book(
    id = this.id,
    category = this.category,
    countryCodes = this.countryCodes.toList(),
    genres = this.genres,
    name = this.name,
    posterUrl = this.posterUrl,
    year = this.year,
    author = this.author
)

fun dev.liinahamari.api.domain.entities.Book.toData(): Book = Book(
    id = this.id,
    category = this.category,
    countryCodes = this.countryCodes.toTypedArray(),
    genres = this.genres,
    name = this.name,
    posterUrl = this.posterUrl,
    year = this.year,
    author = this.author
)
