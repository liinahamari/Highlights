package dev.liinahamari.impl.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.api.domain.usecases.RestoreDatabaseUseCase
import dev.liinahamari.api.domain.usecases.SaveDatabaseUseCase
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
import dev.liinahamari.impl.data.repos.DocumentariesRepo
import dev.liinahamari.impl.data.repos.GamesRepo
import dev.liinahamari.impl.data.repos.MoviesRepo
import dev.liinahamari.impl.domain.DatabaseCountersUseCaseImpl
import dev.liinahamari.impl.domain.RestoreDatabaseUseCaseImpl
import dev.liinahamari.impl.domain.SaveDatabaseUseCaseImpl
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

@Module
interface UseCasesModule {
    companion object {
        @Provides
        @JvmStatic
        fun getAllMoviesUseCase(repo: MoviesRepo): GetMoviesUseCase = GetMoviesUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun getAllDocumentariesUseCase(repo: DocumentariesRepo): GetDocumentariesUseCase =
            GetDocumentariesUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun getAllBooksUseCase(repo: BooksRepo): GetBooksUseCase = GetBooksUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun getAllGamesUseCase(repo: GamesRepo): GetGamesUseCase = GetGamesUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun saveMovieUseCase(repo: MoviesRepo): SaveMovieUseCase = SaveMovieUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun saveDocumentaryUseCase(repo: DocumentariesRepo): SaveDocumentaryUseCase =
            SaveDocumentaryUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun saveBookUseCase(repo: BooksRepo): SaveBookUseCase = SaveBookUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun saveGameUseCase(repo: GamesRepo): SaveGameUseCase = SaveGameUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun deleteMovieUseCase(repo: MoviesRepo): DeleteMovieUseCase = DeleteMovieUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun deleteDocumentaryUseCase(repo: DocumentariesRepo): DeleteDocumentaryUseCase =
            DeleteDocumentaryUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun deleteBookUseCase(repo: BooksRepo): DeleteBookUseCase = DeleteBookUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun deleteGameUseCase(repo: GamesRepo): DeleteGameUseCase = DeleteGameUseCaseImpl(repo)

        @Provides
        @JvmStatic
        fun saveDbUseCase(@Named(APP_CONTEXT) context: Context, db: EntriesDatabase): SaveDatabaseUseCase =
            SaveDatabaseUseCaseImpl(context, db)

        @Provides
        @JvmStatic
        fun restoreDbUseCase(@Named(APP_CONTEXT) context: Context, db: EntriesDatabase): RestoreDatabaseUseCase =
            RestoreDatabaseUseCaseImpl(context, db)

        @Provides
        @JvmStatic
        fun dbCounterUseCase(
            bookDao: BookDao,
            movieDao: MovieDao,
            documentaryDao: DocumentaryDao,
            gameDao: GameDao
        ): DatabaseCountersUseCase = DatabaseCountersUseCaseImpl(bookDao, movieDao, gameDao, documentaryDao)
    }
}
