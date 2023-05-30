package dev.liinahamari.highlights.db.daos

import androidx.room.*
import dev.liinahamari.highlights.ui.main.EntityCategory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity
data class Documentary(
    @PrimaryKey val name: String, override val year: Int, override val category: EntityCategory,
    override val posterUrl: String
): Entry

@Dao
interface DocumentaryDao {
    @Query("SELECT * FROM documentary")
    fun getAll(): Single<List<Documentary>>

    @Query("SELECT * FROM documentary WHERE category = :entityCategory")
    fun getAll(entityCategory: EntityCategory): Single<List<Documentary>>

    @Query("SELECT * FROM documentary WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Single<Documentary>

    @Insert
    fun insertAll(vararg documentaries: Documentary): Completable

    @Insert
    fun insert(documentary: Documentary): Completable

    @Delete
    fun delete(documentary: Documentary): Completable
}
