package dev.liinahamari.suggestions.impl.di.modules

import dagger.Module
import dagger.Provides
import dev.liinahamari.suggestions.impl.data.apis.books.SearchGoogleBooksApi
import dev.liinahamari.suggestions.impl.data.apis.SearchGameApi
import dev.liinahamari.suggestions.impl.data.apis.SearchMovieApi
import dev.liinahamari.suggestions.impl.data.apis.books.SearchOpenLibraryApi
import dev.liinahamari.suggestions.impl.data.db.MovieGenreDao
import dev.liinahamari.suggestions.impl.data.repos.BookRepo
import dev.liinahamari.suggestions.impl.data.repos.BookRepoImpl
import dev.liinahamari.suggestions.impl.data.repos.GameRepo
import dev.liinahamari.suggestions.impl.data.repos.GameRepoImpl
import dev.liinahamari.suggestions.impl.data.repos.MovieRepo
import dev.liinahamari.suggestions.impl.data.repos.MovieRepoImpl
import javax.inject.Singleton

@Module
class RepoModule {
    @Singleton
    @Provides
    fun provideMovieRepo(api: SearchMovieApi, genreDao: MovieGenreDao): MovieRepo = MovieRepoImpl(api, genreDao)

    @Singleton
    @Provides
    fun provideBookRepo(gApi: SearchGoogleBooksApi, olApi: SearchOpenLibraryApi): BookRepo = BookRepoImpl(gApi, olApi)

    @Singleton
    @Provides
    fun provideGameRepo(api: SearchGameApi): GameRepo = GameRepoImpl(api)
}
