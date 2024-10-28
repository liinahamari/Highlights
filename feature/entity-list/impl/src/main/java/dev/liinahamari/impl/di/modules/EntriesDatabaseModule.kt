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
import dev.liinahamari.impl.data.db.daos.ShortsDao
import javax.inject.Named
import javax.inject.Singleton

@Module
class EntriesDatabaseModule {
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
                    db.execSQL("ALTER TABLE movie ADD COLUMN tmdbId INTEGER")
                    db.execSQL("ALTER TABLE documentary ADD COLUMN tmdbId INTEGER")
                }
            }
        ).addMigrations(
            object : Migration(4, 5) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("ALTER TABLE game DROP COLUMN countryCodes")
                }
            }
        ).addMigrations(
            object : Migration(5, 6)/*todo make last migration version to EntriesDatabase argu*/ {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("CREATE TABLE IF NOT EXISTS `short` (`name` TEXT NOT NULL, `description` TEXT NOT NULL, `year` INTEGER NOT NULL, `category` TEXT NOT NULL, `posterUrl` TEXT, `countryCodes` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tmdbUrl` TEXT, `tmdbId` INTEGER NOT NULL)")
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
    fun shortsDao(db: EntriesDatabase): ShortsDao = db.shortsDao()

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
