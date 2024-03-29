package com.example.suggestions

import dev.liinahamari.suggestions.api.MovieSuggestionsDependencies
import dev.liinahamari.suggestions.api.SuggestionsApi
import dev.liinahamari.suggestions.impl.di.create

object MovieSuggestionsListFactory {
    @JvmStatic fun getApi(deps: MovieSuggestionsDependencies) = SuggestionsApi.create(deps)
}
