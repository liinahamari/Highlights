package dev.liinahamari.impl.data.db.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.MovieGenre
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity
data class Movie(
    val name: String,
    val genres: List<MovieGenre>,
    override val year: Int,
    override val category: Category,
    override val posterUrl: String,
    override val countryCodes: Array<String>,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
) : Entry {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (name != other.name) return false
        if (genres != other.genres) return false
        if (year != other.year) return false
        if (category != other.category) return false
        if (posterUrl != other.posterUrl) return false
        if (!countryCodes.contentEquals(other.countryCodes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + genres.hashCode()
        result = 31 * result + year
        result = 31 * result + category.hashCode()
        result = 31 * result + posterUrl.hashCode()
        result = 31 * result + countryCodes.contentHashCode()
        return result
    }
}

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

fun Movie.toDomain(): dev.liinahamari.api.domain.entities.Movie = dev.liinahamari.api.domain.entities.Movie(
    id = this.id,
    category = this.category,
    countryCodes = this.countryCodes.toList(),
    genres = this.genres,
    name = this.name,
    posterUrl = this.posterUrl,
    year = this.year
)
