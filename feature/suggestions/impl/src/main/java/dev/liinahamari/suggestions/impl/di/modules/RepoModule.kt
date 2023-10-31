package dev.liinahamari.suggestions.impl.di.modules

import dagger.Module
import dagger.Provides
import dev.liinahamari.suggestions.impl.data.SearchMovieApi
import dev.liinahamari.suggestions.impl.data.SearchRepo
import dev.liinahamari.suggestions.impl.data.SearchRepoImpl
import javax.inject.Singleton

@Module
class RepoModule {
    @Singleton
    @Provides
    fun provideSearchRepository(api: SearchMovieApi): SearchRepo = SearchRepoImpl(api = api)
}
