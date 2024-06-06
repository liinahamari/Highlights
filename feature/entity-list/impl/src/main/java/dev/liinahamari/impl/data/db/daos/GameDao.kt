package dev.liinahamari.impl.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.models.Game
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface GameDao {
    @Query("SELECT * FROM game WHERE category = :category")
    fun getAll(category: Category): Single<List<Game>>

    @Query("SELECT * FROM game WHERE category = :category and countryCodes LIKE '%' || :countryCode || '%'")
    fun filterByCountry(category: Category, countryCode: String): Single<List<Game>>

    @Query("SELECT * FROM game WHERE id LIKE :id and category = :category LIMIT 1")
    fun findById(category: Category, id: Long): Single<Game>

    @Query("SELECT * FROM game WHERE id IN (:ids) and category = :category")
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Game>>

    @Insert(onConflict = REPLACE)
    fun insert(games: List<Game>): Completable

    @Query("DELETE FROM game WHERE id = :id")
    fun delete(id: Long): Completable

    @Query("SELECT COUNT(id) FROM game")
    suspend fun getRowCount(): Int
}
