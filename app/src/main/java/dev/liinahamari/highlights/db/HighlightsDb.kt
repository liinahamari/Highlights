package dev.liinahamari.highlights.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.liinahamari.highlights.db.daos.Book
import dev.liinahamari.highlights.db.daos.BookDao
import dev.liinahamari.highlights.db.daos.Documentary
import dev.liinahamari.highlights.db.daos.DocumentaryDao
import dev.liinahamari.highlights.db.daos.Game
import dev.liinahamari.highlights.db.daos.GameDao
import dev.liinahamari.highlights.db.daos.Movie
import dev.liinahamari.highlights.db.daos.MovieDao
import dev.liinahamari.highlights.ui.main.EntityCategory


//todo backup
const val DATABASE_NAME = "entries-db"

@TypeConverters(CategoryConverters::class, CountryConverter::class)
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

class CountryConverter {
    @TypeConverter fun fromString(value: String): Array<String> =
        Gson().fromJson(value, object : TypeToken<Array<String>?>() {}.type)

    @TypeConverter fun fromArrayList(array: Array<String>): String = Gson().toJson(array)
}
