package dev.liinahamari.suggestions.impl.di.modules

import dagger.Binds
import dagger.Module
import dev.liinahamari.suggestions.api.SearchMovieUseCase
import dev.liinahamari.suggestions.impl.usecases.SearchMovieUseCaseImpl
import javax.inject.Singleton

@Module
interface UseCaseModule {
    @Binds
    @Singleton
    fun searchMoviesUseCase(impl: SearchMovieUseCaseImpl): SearchMovieUseCase
}
