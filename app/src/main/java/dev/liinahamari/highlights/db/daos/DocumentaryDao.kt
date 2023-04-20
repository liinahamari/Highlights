package dev.liinahamari.highlights.db.daos

import androidx.room.*

@Entity
data class Documentary(@PrimaryKey val name: String, val year: Int)

@Dao
interface DocumentaryDao {
    @Query("SELECT * FROM documentary")
    fun getAll(): List<Documentary>

    @Query("SELECT * FROM documentary WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Documentary

    @Insert
    fun insertAll(vararg documentaries: Documentary)

    @Insert
    fun insert(documentary: Documentary)

    @Delete
    fun delete(documentary: Documentary)
}
