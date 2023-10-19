package dev.liinahamari.impl.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.liinahamari.impl.data.db.daos.Book
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.Documentary
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.Game
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.Movie
import dev.liinahamari.impl.data.db.daos.MovieDao

@TypeConverters(
    CategoryConverters::class,
    CountryConverter::class,
    MovieGenreConverters::class,
    BookGenreConverters::class,
    GameGenreConverters::class
)
@Database(entities = [Book::class, Documentary::class, Movie::class, Game::class], version = 1)
abstract class EntriesDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun documentaryDao(): DocumentaryDao
    abstract fun gameDao(): GameDao
    abstract fun movieDao(): MovieDao
}
