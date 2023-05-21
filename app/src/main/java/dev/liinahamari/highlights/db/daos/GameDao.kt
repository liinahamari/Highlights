package dev.liinahamari.highlights.db.daos

import androidx.room.*
import dev.liinahamari.highlights.ui.main.EntityCategory

@Entity
data class Game(
    @PrimaryKey val name: String, val genre: String, override val year: Int, override val category: EntityCategory,
    override val posterUrl: String
) : Entry

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): List<Game>

    @Query("SELECT * FROM game WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Game

    @Insert
    fun insertAll(vararg games: Game)

    @Insert
    fun insert(game: Game)

    @Delete
    fun delete(game: Game)
}
