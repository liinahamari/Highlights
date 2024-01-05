package dev.liinahamari.suggestions.impl.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Database(entities = [MovieGenre::class], version = 1)
abstract class MovieGenresDb : RoomDatabase() {
    abstract fun movieGenreDao(): MovieGenreDao
}

@Dao
interface MovieGenreDao {
    @Query("SELECT COUNT(name) FROM moviegenre")
    fun count(): Single<Int>

    @Query("SELECT * FROM moviegenre")
    fun getAll(): Single<List<MovieGenre>>

    @Query("SELECT * FROM moviegenre WHERE id LIKE :id")
    fun findById(id: Long): Single<MovieGenre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieGenres: List<MovieGenre>): Completable
}

@Entity
data class MovieGenre(@PrimaryKey val id: Int, val name: String)
