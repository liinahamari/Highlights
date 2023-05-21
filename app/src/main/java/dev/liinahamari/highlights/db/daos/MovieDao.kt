package dev.liinahamari.highlights.db.daos

import androidx.room.*
import dev.liinahamari.highlights.ui.main.EntityCategory

@Entity
data class Movie(
    @PrimaryKey val name: String, val genre: String, override val year: Int, override val category: EntityCategory,
    override val posterUrl: String
) : Entry

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Movie

    @Insert
    fun insertAll(vararg movies: Movie)

    @Insert
    fun insert(movie: Movie)

    @Delete
    fun delete(movie: Movie)
}
