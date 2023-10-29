package dev.liinahamari.movies_suggestions.impl.di.modules

import dagger.Binds
import dagger.Module
import dev.liinahamari.movies_suggestions.api.SearchMovieUseCase
import dev.liinahamari.movies_suggestions.impl.usecases.SearchMovieUseCaseImpl
import javax.inject.Singleton

@Module
interface UseCaseModule {
    @Binds
    @Singleton
    fun searchMoviesUseCase(impl: SearchMovieUseCaseImpl): SearchMovieUseCase
}
