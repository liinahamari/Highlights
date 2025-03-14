package dev.liinahamari.impl.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.models.Movie
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie WHERE category = :category")
    fun getAll(category: Category): Single<List<Movie>>

    @Query("SELECT * FROM movie WHERE category = :category and countryCodes LIKE '%' || :countryCode || '%'")
    fun filterByCountry(category: Category, countryCode: String): Single<List<Movie>>

    @Query("SELECT * FROM movie WHERE id LIKE :id and category = :category LIMIT 1")
    fun findById(category: Category, id: Long): Single<Movie>

    @Query("SELECT * FROM movie WHERE category = :category and id IN (:ids)")
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Movie>>

    @Insert(onConflict = REPLACE)
    fun insert(movies: List<Movie>): Completable

    @Query("DELETE FROM movie WHERE id = :id")
    fun delete(id: Long): Completable

    @Query("SELECT COUNT(id) FROM movie")
    suspend fun getRowCount(): Int

    @Query("SELECT COUNT(id) FROM movie WHERE category = :categoryGood OR category = :categorySo")
    suspend fun getRowActualCount(categoryGood: Category = Category.GOOD, categorySo: Category = Category.SO_SO): Int
}
