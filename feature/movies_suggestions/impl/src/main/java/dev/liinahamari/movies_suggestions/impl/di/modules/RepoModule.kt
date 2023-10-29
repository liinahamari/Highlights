package dev.liinahamari.movies_suggestions.impl.di.modules

import dagger.Module
import dagger.Provides
import dev.liinahamari.movies_suggestions.impl.data.SearchMovieApi
import dev.liinahamari.movies_suggestions.impl.data.SearchRepo
import dev.liinahamari.movies_suggestions.impl.data.SearchRepoImpl
import javax.inject.Singleton

@Module
class RepoModule {
    @Singleton
    @Provides
    fun provideSearchRepository(api: SearchMovieApi): SearchRepo = SearchRepoImpl(api = api)
}
