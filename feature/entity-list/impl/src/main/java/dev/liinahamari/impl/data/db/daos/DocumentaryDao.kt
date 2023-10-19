package dev.liinahamari.impl.data.db.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.models.Documentary
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface DocumentaryDao {
    @Query("SELECT * FROM documentary WHERE category = :category")
    fun getAll(category: Category): Single<List<Documentary>>

    @Query("SELECT * FROM documentary WHERE category = :category and name LIKE :name LIMIT 1")
    fun findByName(category: Category, name: String): Single<Documentary>

    @Insert(onConflict = REPLACE)
    fun insert(documentary: Documentary): Completable

    @Query("DELETE FROM documentary WHERE name = :id")
    fun delete(id: String): Completable
}
