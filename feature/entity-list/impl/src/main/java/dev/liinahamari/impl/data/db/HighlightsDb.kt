package dev.liinahamari.impl.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.MovieDao
import dev.liinahamari.impl.data.db.daos.ShortsDao
import dev.liinahamari.impl.data.db.daos.models.Book
import dev.liinahamari.impl.data.db.daos.models.Documentary
import dev.liinahamari.impl.data.db.daos.models.Game
import dev.liinahamari.impl.data.db.daos.models.Movie
import dev.liinahamari.impl.data.db.daos.models.Short

@TypeConverters(
    CategoryConverters::class,
    CountryConverter::class,
    MovieGenreConverters::class,
    BookGenreConverters::class,
    GameGenreConverters::class
)
@Database(entities = [Book::class, Documentary::class, Movie::class, Game::class, Short::class], version = 6)
abstract class EntriesDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun shortsDao(): ShortsDao
    abstract fun documentaryDao(): DocumentaryDao
    abstract fun gameDao(): GameDao
    abstract fun movieDao(): MovieDao
}
