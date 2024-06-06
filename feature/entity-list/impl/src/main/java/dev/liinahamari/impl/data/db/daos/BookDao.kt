package dev.liinahamari.impl.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.models.Book
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface BookDao {
    @Query("SELECT * FROM book WHERE category = :ctg")
    fun getAll(ctg: Category): Single<List<Book>>

    @Query("SELECT * FROM book WHERE category = :category and countryCodes LIKE '%' || :countryCode || '%'")
    fun filterByCountry(category: Category, countryCode: String): Single<List<Book>>

    @Query("SELECT * FROM book WHERE id LIKE :id and category = :ctg LIMIT 1")
    fun findById(ctg: Category, id: Long): Single<Book>

    @Query("SELECT * FROM book WHERE id IN (:ids) and category = :ctg")
    fun findByIds(ctg: Category, ids: Set<Long>): Single<List<Book>>

    @Insert(onConflict = REPLACE)
    fun insert(books: List<Book>): Completable //todo test vararg

    @Query("DELETE FROM book WHERE id = :id")
    fun delete(id: Long): Completable

    @Query("SELECT COUNT(id) FROM book")
    suspend fun getRowCount(): Int
}
