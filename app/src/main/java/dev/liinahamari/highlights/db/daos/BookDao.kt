package dev.liinahamari.highlights.db.daos

import androidx.room.*

@Entity
data class Book(@PrimaryKey val name: String, val genre: String, val author: String, val year: Int)

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Book

    @Insert
    fun insertAll(vararg entries: Book)

    @Insert
    fun insert(book: Book)

    @Delete
    fun delete(book: Book)
}
