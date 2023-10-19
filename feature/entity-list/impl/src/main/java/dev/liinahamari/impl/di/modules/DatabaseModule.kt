package dev.liinahamari.impl.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.liinahamari.api.domain.usecases.GetAllBooksUseCase
import dev.liinahamari.api.domain.usecases.GetAllDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.GetAllGamesUseCase
import dev.liinahamari.api.domain.usecases.GetAllMoviesUseCase
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
import dev.liinahamari.impl.domain.GetAllBooksUseCaseImpl
import dev.liinahamari.impl.domain.GetAllDocumentariesUseCaseImpl
import dev.liinahamari.impl.domain.GetAllGamesUseCaseImpl
import dev.liinahamari.impl.domain.GetAllMoviesUseCaseImpl
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

@Module
interface UseCasesModule {
    companion object {
        @Provides
        @JvmStatic
        fun getAllMoviesUseCase(repo: MoviesRepo): GetAllMoviesUseCase = GetAllMoviesUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun getAllDocumentariesUseCase(repo: DocumentariesRepo): GetAllDocumentariesUseCase =
            GetAllDocumentariesUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun getAllBooksUseCase(repo: BooksRepo): GetAllBooksUseCase = GetAllBooksUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun getAllGamesUseCase(repo: GamesRepo): GetAllGamesUseCase = GetAllGamesUseCaseImpl(repo)
    }
}

@Module
interface RepoModule {
    @Binds
    @Singleton
    fun moviesRepo(impl: MoviesRepoImpl): MoviesRepo

    @Binds
    @Singleton
    fun gamesRepo(impl: GamesRepoImpl): GamesRepo

    @Binds
    @Singleton
    fun documentariesRepo(impl: DocumentariesRepoImpl): DocumentariesRepo

    @Binds
    @Singleton
    fun booksRepo(impl: BooksRepoImpl): BooksRepo
}
