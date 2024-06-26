package dev.liinahamari.impl.di.modules

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dev.liinahamari.api.DATABASE_NAME
import dev.liinahamari.impl.data.db.EntriesDatabase
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.MovieDao
import javax.inject.Named
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun database(@Named(APP_CONTEXT) context: Context): EntriesDatabase = Room.databaseBuilder(
        context,
        EntriesDatabase::class.java, DATABASE_NAME,
    )
        .addMigrations(object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE movie ADD COLUMN tmdbUrl TEXT")
                db.execSQL("ALTER TABLE documentary ADD COLUMN tmdbUrl TEXT")
            }
        }
        ).addMigrations(
            object : Migration(3, 4) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("ALTER TABLE movie ADD COLUMN tmdbId INTEGER NOT NULL")
                    db.execSQL("ALTER TABLE documentary ADD COLUMN tmdbId INTEGER NOT NULL")
                }
            }
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
