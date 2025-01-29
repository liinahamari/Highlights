package dev.liinahamari.impl.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.models.Short
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ShortsDao {
    @Query("SELECT * FROM short WHERE category = :category")
    fun getAll(category: Category): Single<List<Short>>

    @Query("SELECT * FROM short WHERE category = :category and countryCodes LIKE '%' || :countryCode || '%'")
    fun filterByCountry(category: Category, countryCode: String): Single<List<Short>>

    @Query("SELECT * FROM short WHERE id LIKE :id and category = :category LIMIT 1")
    fun findById(category: Category, id: Long): Single<Short>

    @Query("SELECT * FROM short WHERE category = :category and id IN (:ids)")
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Short>>

    @Insert(onConflict = REPLACE)
    fun insert(movies: List<Short>): Completable

    @Query("DELETE FROM short WHERE id = :id")
    fun delete(id: Long): Completable

    @Query("SELECT COUNT(id) FROM short")
    suspend fun getRowCount(): Int

    @Query("SELECT COUNT(id) FROM short WHERE category = :categoryGood OR category = :categorySo")
    suspend fun getRowActualCount(categoryGood: Category = Category.GOOD, categorySo: Category = Category.SO_SO): Int
}
