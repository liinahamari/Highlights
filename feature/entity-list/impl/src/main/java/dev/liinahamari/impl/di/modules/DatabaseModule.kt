package dev.liinahamari.impl.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.liinahamari.api.domain.usecases.delete.DeleteBookUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteGameUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteMovieUseCase
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.api.domain.usecases.save.SaveBookUseCase
import dev.liinahamari.api.domain.usecases.save.SaveDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.save.SaveGameUseCase
import dev.liinahamari.api.domain.usecases.save.SaveMovieUseCase
import dev.liinahamari.impl.data.db.EntriesDatabase
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.GameDao
import dev.liinahamari.impl.data.db.daos.MovieDao
import dev.liinahamari.impl.data.repos.BooksRepo
import dev.liinahamari.impl.data.repos.BooksRepoImpl
import dev.liinahamari.impl.data.repos.DocumentariesRepo
import dev.liinahamari.impl.data.repos.DocumentariesRepoImpl
import dev.liinahamari.impl.data.repos.GamesRepo
import dev.liinahamari.impl.data.repos.GamesRepoImpl
import dev.liinahamari.impl.data.repos.MoviesRepo
import dev.liinahamari.impl.data.repos.MoviesRepoImpl
import dev.liinahamari.impl.domain.delete.DeleteBookUseCaseImpl
import dev.liinahamari.impl.domain.delete.DeleteDocumentaryUseCaseImpl
import dev.liinahamari.impl.domain.delete.DeleteGameUseCaseImpl
import dev.liinahamari.impl.domain.delete.DeleteMovieUseCaseImpl
import dev.liinahamari.impl.domain.get.GetBooksUseCaseImpl
import dev.liinahamari.impl.domain.get.GetDocumentariesUseCaseImpl
import dev.liinahamari.impl.domain.get.GetGamesUseCaseImpl
import dev.liinahamari.impl.domain.get.GetMoviesUseCaseImpl
import dev.liinahamari.impl.domain.save.SaveBookUseCaseImpl
import dev.liinahamari.impl.domain.save.SaveDocumentaryUseCaseImpl
import dev.liinahamari.impl.domain.save.SaveGameUseCaseImpl
import dev.liinahamari.impl.domain.save.SaveMovieUseCaseImpl
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
