package dev.liinahamari.highlights.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.liinahamari.highlights.db.daos.*

@Database(entities = [Book::class, Documentary::class, Movie::class, Game::class], version = 1)
abstract class EntriesDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun documentaryDao(): DocumentaryDao
    abstract fun gameDao(): GameDao
    abstract fun movieDao(): MovieDao
}
