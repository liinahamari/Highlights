package dev.liinahamari.highlights.db.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import dev.liinahamari.highlights.ui.single_entity.EntityCategory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity
data class Documentary(
    val name: String,
    override val year: Int,
    override val category: EntityCategory,
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
    @Query("SELECT * FROM documentary")
    fun getAll(): Single<List<Documentary>>

    @Query("SELECT * FROM documentary WHERE category = :entityCategory")
    fun getAll(entityCategory: EntityCategory): Single<List<Documentary>>

    @Query("SELECT * FROM documentary WHERE category = :entityCategory and name LIKE :name LIMIT 1")
    fun findByName(entityCategory: EntityCategory, name: String): Single<Documentary>

    @Insert
    fun insertAll(vararg documentaries: Documentary): Completable

    @Insert(onConflict = REPLACE)
    fun insert(documentary: Documentary): Completable

    @Delete
    fun delete(documentary: Documentary): Completable

    @Query("DELETE FROM documentary WHERE name = :id")
    fun delete(id: String): Completable
}
