package dev.liinahamari.suggestions.impl.di

import dagger.Component
import dev.liinahamari.suggestions.api.MovieSuggestionsDependencies
import dev.liinahamari.suggestions.api.SuggestionsApi
import dev.liinahamari.suggestions.impl.di.modules.DbModule
import dev.liinahamari.suggestions.impl.di.modules.NetworkModule
import dev.liinahamari.suggestions.impl.di.modules.RepoModule
import dev.liinahamari.suggestions.impl.di.modules.UseCaseModule
import javax.inject.Singleton

@Component(
    modules = [DbModule::class, NetworkModule::class, UseCaseModule::class, RepoModule::class],
    dependencies = [MovieSuggestionsDependencies::class],
)
@Singleton
internal interface MoviesSuggestionsComponent : SuggestionsApi {
    @Component.Factory
    interface Factory {
        fun create(deps: MovieSuggestionsDependencies): MoviesSuggestionsComponent
    }
}

fun SuggestionsApi.Companion.create(deps: MovieSuggestionsDependencies): SuggestionsApi =
    DaggerMoviesSuggestionsComponent.factory().create(deps)
