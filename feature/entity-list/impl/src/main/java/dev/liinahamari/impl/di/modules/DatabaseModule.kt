package dev.liinahamari.impl.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.liinahamari.impl.data.db.EntriesDatabase
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.MovieDao
import javax.inject.Named
import javax.inject.Singleton

private const val DATABASE_NAME = "entries-db"
private const val APP_CONTEXT = "app_ctx"

@Module
class DatabaseModule {
    @Provides
    @Singleton
    @Named(APP_CONTEXT)
    fun bindContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun database(@Named(APP_CONTEXT) context: Context): EntriesDatabase = Room.databaseBuilder(
        context,
        EntriesDatabase::class.java, DATABASE_NAME,
    )
        .fallbackToDestructiveMigration()
        .build()

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
