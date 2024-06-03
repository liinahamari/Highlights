package dev.liinahamari.impl.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.models.Documentary
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface DocumentaryDao {
    @Query("SELECT * FROM documentary WHERE category = :category")
    fun getAll(category: Category): Single<List<Documentary>>

    @Query("SELECT * FROM documentary WHERE category = :category and countryCodes LIKE '%' || :countryCode || '%'")
    fun filterByCountry(category: Category, countryCode: String): Single<List<Documentary>>

    @Query("SELECT * FROM documentary WHERE category = :category and id LIKE :id LIMIT 1")
    fun findById(category: Category, id: Long): Single<Documentary>

    @Query("SELECT * FROM documentary WHERE category = :category and id IN (:ids)")
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Documentary>>

    @Insert(onConflict = REPLACE)
    fun insert(documentaries: List<Documentary>): Completable

    @Query("DELETE FROM documentary WHERE id = :id")
    fun delete(id: Long): Completable

    @Query("SELECT COUNT(id) FROM documentary")
    fun getRowCount(): Single<Int>
}
