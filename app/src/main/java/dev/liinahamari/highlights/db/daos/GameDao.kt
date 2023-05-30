package dev.liinahamari.highlights.db.daos

import androidx.room.*
import dev.liinahamari.highlights.ui.main.EntityCategory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity
data class Game(
    @PrimaryKey val name: String, val genre: String, override val year: Int, override val category: EntityCategory,
    override val posterUrl: String
) : Entry

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): Single<List<Game>>
    @Query("SELECT * FROM game WHERE category = :entityCategory")
    fun getAll(entityCategory: EntityCategory): Single<List<Game>>

    @Query("SELECT * FROM game WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Single<Game>

    @Insert
    fun insertAll(vararg games: Game): Completable

    @Insert
    fun insert(game: Game): Completable

    @Delete
    fun delete(game: Game): Completable
}
