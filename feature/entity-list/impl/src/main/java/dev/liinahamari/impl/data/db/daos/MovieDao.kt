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

    @Query("SELECT * FROM movie WHERE id LIKE :id and category = :category LIMIT 1")
    fun findById(category: Category, id: Long): Single<Movie>

    @Query("SELECT * FROM movie WHERE category = :category and id IN (:ids)")
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Movie>>

    @Insert(onConflict = REPLACE)
    fun insert(movies: List<Movie>): Completable

    @Query("DELETE FROM movie WHERE id = :id")
    fun delete(id: Long): Completable

    @Query("SELECT COUNT(id) FROM movie")
    fun getRowCount(): Single<Int>
}
