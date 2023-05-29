package dev.liinahamari.highlights.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import dev.liinahamari.highlights.db.daos.*
import dev.liinahamari.highlights.ui.main.EntityCategory

const val DATABASE_NAME = "entries-db"
@TypeConverters(CategoryConverters::class)
@Database(entities = [Book::class, Documentary::class, Movie::class, Game::class], version = 1)
abstract class EntriesDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun documentaryDao(): DocumentaryDao
    abstract fun gameDao(): GameDao
    abstract fun movieDao(): MovieDao
}

class CategoryConverters {
    @TypeConverter
    fun toCategory(value: String) = enumValueOf<EntityCategory>(value)

    @TypeConverter
    fun fromCategory(value: EntityCategory) = value.name
}
