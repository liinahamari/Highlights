package dev.liinahamari.feature.db_backup.sample

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Database(entities = [Book::class], version = 1)
abstract class TestDb : RoomDatabase() {
    abstract fun bookDao(): BookDao
}

@Entity
data class Book(@PrimaryKey val id: Int = 0)

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): Single<List<Book>>

    @Insert
    fun insertAll(vararg entries: Book): Completable

    @Insert
    fun insert(book: Book): Completable

    @Query("DELETE FROM book")
    fun deleteAll(): Completable
}
