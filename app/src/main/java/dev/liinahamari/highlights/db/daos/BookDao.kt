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
    override val posterUrl: String
): Entry

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
