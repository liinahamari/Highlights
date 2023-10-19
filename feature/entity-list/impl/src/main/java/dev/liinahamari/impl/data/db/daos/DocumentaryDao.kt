package dev.liinahamari.impl.data.db.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import dev.liinahamari.api.domain.entities.Category
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity
data class Documentary(
    val name: String,
    override val year: Int,
    override val category: Category,
    override val posterUrl: String,
    override val countryCodes: Array<String>,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
) : Entry {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Documentary

        if (name != other.name) return false
        if (year != other.year) return false
        if (category != other.category) return false
        if (posterUrl != other.posterUrl) return false
        if (!countryCodes.contentEquals(other.countryCodes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + year
        result = 31 * result + category.hashCode()
        result = 31 * result + posterUrl.hashCode()
        result = 31 * result + countryCodes.contentHashCode()
        return result
    }
}

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

fun Documentary.toDomain(): dev.liinahamari.api.domain.entities.Documentary =
    dev.liinahamari.api.domain.entities.Documentary(
        id = this.id,
        category = this.category,
        countryCodes = this.countryCodes.toList(),
        name = this.name,
        posterUrl = this.posterUrl,
        year = this.year
    )
