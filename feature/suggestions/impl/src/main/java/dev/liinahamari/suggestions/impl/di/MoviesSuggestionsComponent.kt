package dev.liinahamari.suggestions.impl.di

import dagger.Component
import dev.liinahamari.suggestions.api.SuggestionsApi
import dev.liinahamari.suggestions.impl.di.modules.NetworkModule
import dev.liinahamari.suggestions.impl.di.modules.RepoModule
import dev.liinahamari.suggestions.impl.di.modules.UseCaseModule
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, UseCaseModule::class, RepoModule::class])
@Singleton
internal interface MoviesSuggestionsComponent : SuggestionsApi {
    @Component.Factory
    interface Factory {
        fun create(): MoviesSuggestionsComponent
    }
}

fun SuggestionsApi.Companion.create(): SuggestionsApi =
    DaggerMoviesSuggestionsComponent.factory().create()
