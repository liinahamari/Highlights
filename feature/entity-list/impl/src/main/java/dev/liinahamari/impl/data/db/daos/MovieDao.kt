package dev.liinahamari.impl.data.db.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
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

    @Insert(onConflict = REPLACE)
    fun insert(movie: Movie): Completable

    @Query("DELETE FROM movie WHERE id = :id")
    fun delete(id: Long): Completable
}

