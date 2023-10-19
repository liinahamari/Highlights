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

    @Query("SELECT * FROM movie WHERE name LIKE :name and category = :category LIMIT 1")
    fun findByName(category: Category, name: String): Single<Movie>

    @Insert(onConflict = REPLACE)
    fun insert(movie: Movie): Completable

    @Query("DELETE FROM movie WHERE name = :id")
    fun delete(id: String): Completable
}

