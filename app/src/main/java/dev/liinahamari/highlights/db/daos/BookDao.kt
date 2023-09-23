package dev.liinahamari.highlights.db.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import dev.liinahamari.highlights.ui.single_entity.EntityCategory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity
data class Book(
    val name: String,
    val genres: List<BookGenre>,
    val author: String,
    override val year: Int,
    override val category: EntityCategory,
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

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): Single<List<Book>>

    @Query("SELECT * FROM book WHERE category = :entityCategory")
    fun getAll(entityCategory: EntityCategory): Single<List<Book>>

    @Query("SELECT * FROM book WHERE name LIKE :name and category = :entityCategory LIMIT 1")
    fun findByName(entityCategory: EntityCategory, name: String): Single<Book>

    @Insert
    fun insertAll(vararg entries: Book): Completable

    @Insert(onConflict = REPLACE)
    fun insert(book: Book): Completable

    @Delete
    fun delete(book: Book): Completable

    @Query("DELETE FROM book WHERE name = :id")
    fun delete(id: String): Completable
}

enum class BookGenre {
    FICTION,
    SCIENCE_FICTION,
    THRILLER,
    HISTORY,
    HISTORICAL_FICTION,
    FANTASY,
    MEMOIR,
    SHORT_STORIES,
    HUMOR,
    BIOGRAPHY,
    SPIRITUALITY,
    TRAVEL_LITERATURE,
    MAGICAL_REALISM,
    WESTERN_FICTION,
    LITERATURE_REALISM,
    SOCIAL_SCIENCE,
    DYSTOPIAN_FICTION,
    PHILOSOPHY
}
