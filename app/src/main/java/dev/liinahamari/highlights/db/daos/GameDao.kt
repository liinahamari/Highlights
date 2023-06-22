package dev.liinahamari.highlights.db.daos

import androidx.room.*
import dev.liinahamari.highlights.ui.single_entity.EntityCategory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity
data class Game(
    @PrimaryKey val name: String,
    val genres: List<GameGenre>,
    override val year: Int,
    override val category: EntityCategory,
    override val posterUrl: String,
    override val countryCodes: Array<String>
) : Entry {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

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
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): Single<List<Game>>
    @Query("SELECT * FROM game WHERE category = :entityCategory")
    fun getAll(entityCategory: EntityCategory): Single<List<Game>>

    @Query("SELECT * FROM game WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Single<Game>

    @Insert
    fun insertAll(vararg games: Game): Completable

    @Insert
    fun insert(game: Game): Completable

    @Delete
    fun delete(game: Game): Completable

    @Query("DELETE FROM game WHERE name = :id")
    fun delete(id: String): Completable
}

enum class GameGenre {
    ADVENTURE,
    FIGHTING,
    FPS,
    RPG,
    SIMULATION,
    RTS,
    TPS,
    STRATEGY,
    SURVIVAL_HORROR
}
