package dev.liinahamari.suggestions.impl.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.liinahamari.suggestions.impl.data.db.MovieGenreDao
import dev.liinahamari.suggestions.impl.data.db.MovieGenresDb
import javax.inject.Named
import javax.inject.Singleton

private const val DATABASE_NAME = "movie-genres-db"
private const val APP_CONTEXT = "app_ctx"

@Module class DbModule {
    @Provides
    @Singleton
    @Named(APP_CONTEXT)
    fun bindContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun database(@Named(APP_CONTEXT) context: Context): MovieGenresDb = Room.databaseBuilder(
        context,
        MovieGenresDb::class.java, DATABASE_NAME,
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun genreDao(db: MovieGenresDb): MovieGenreDao = db.movieGenreDao()
}
