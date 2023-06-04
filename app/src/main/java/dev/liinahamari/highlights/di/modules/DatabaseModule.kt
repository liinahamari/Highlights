package dev.liinahamari.highlights.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.liinahamari.highlights.db.EntriesDatabase
import dev.liinahamari.highlights.db.daos.BookDao
import dev.liinahamari.highlights.db.daos.DocumentaryDao
import dev.liinahamari.highlights.db.daos.GameDao
import dev.liinahamari.highlights.db.daos.MovieDao
import javax.inject.Named
import javax.inject.Singleton

//todo backup
private const val DATABASE_NAME = "entries-db"

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun database(@Named(APP_CONTEXT) context: Context): EntriesDatabase = Room.databaseBuilder(
        context,
        EntriesDatabase::class.java, DATABASE_NAME,
    ).build()

    @Provides
    @Singleton
    fun bookDao(db: EntriesDatabase): BookDao = db.bookDao()

    @Provides
    @Singleton
    fun documentaryDao(db: EntriesDatabase): DocumentaryDao = db.documentaryDao()

    @Provides
    @Singleton
    fun gameDao(db: EntriesDatabase): GameDao = db.gameDao()

    @Provides
    @Singleton
    fun movieDao(db: EntriesDatabase): MovieDao = db.movieDao()
}
