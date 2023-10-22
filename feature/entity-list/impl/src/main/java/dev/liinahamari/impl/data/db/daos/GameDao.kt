package dev.liinahamari.impl.data.db.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.models.Game
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface GameDao {
    @Query("SELECT * FROM game WHERE category = :category")
    fun getAll(category: Category): Single<List<Game>>

    @Query("SELECT * FROM game WHERE id LIKE :id and category = :category LIMIT 1")
    fun findById(category: Category, id: Long): Single<Game>

    @Insert(onConflict = REPLACE)
    fun insert(game: Game): Completable

    @Query("DELETE FROM game WHERE id = :id")
    fun delete(id: Long): Completable
}
