package dev.liinahamari.impl.data.db.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.models.Book
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface BookDao {
    @Query("SELECT * FROM book WHERE category = :ctg")
    fun getAll(ctg: Category): Single<List<Book>>

    @Query("SELECT * FROM book WHERE id LIKE :id and category = :ctg LIMIT 1")
    fun findById(ctg: Category, id: Long): Single<Book>

    @Insert(onConflict = REPLACE)
    fun insert(book: Book): Completable

    @Query("DELETE FROM book WHERE id = :id")
    fun delete(id: Long): Completable
}

