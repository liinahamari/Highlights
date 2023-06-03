package dev.liinahamari.highlights.db.daos

import androidx.room.*
import dev.liinahamari.highlights.ui.main.EntityCategory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity
data class Book(
    @PrimaryKey val name: String,
    val genre: String,
    val author: String,
    override val year: Int,
    override val category: EntityCategory,
    override val posterUrl: String,
    override val countryCodes: Array<String>
): Entry {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (name != other.name) return false
        if (genre != other.genre) return false
        if (author != other.author) return false
        if (year != other.year) return false
        if (category != other.category) return false
        if (posterUrl != other.posterUrl) return false
        if (!countryCodes.contentEquals(other.countryCodes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + genre.hashCode()
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

    @Query("SELECT * FROM book WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Single<Book>

    @Insert
    fun insertAll(vararg entries: Book): Completable

    @Insert
    fun insert(book: Book): Completable

    @Delete
    fun delete(book: Book): Completable
}
