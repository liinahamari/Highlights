package dev.liinahamari.suggestions.impl.di.modules

import dagger.Module
import dagger.Provides
import dev.liinahamari.suggestions.impl.data.apis.SearchBookApi
import dev.liinahamari.suggestions.impl.data.apis.SearchGameApi
import dev.liinahamari.suggestions.impl.data.apis.SearchMovieApi
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
    fun provideMovieRepo(api: SearchMovieApi): MovieRepo = MovieRepoImpl(api)

    @Singleton
    @Provides
    fun provideBookRepo(api: SearchBookApi): BookRepo = BookRepoImpl(api)

    @Singleton
    @Provides
    fun provideGameRepo(api: SearchGameApi): GameRepo = GameRepoImpl(api)
}
