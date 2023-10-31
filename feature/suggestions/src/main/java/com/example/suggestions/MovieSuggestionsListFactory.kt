package com.example.movies_suggestions

import dev.liinahamari.suggestions.api.MovieSuggestionsApi
import dev.liinahamari.suggestions.impl.di.create

object MovieSuggestionsListFactory {
    @JvmStatic fun getApi() = MovieSuggestionsApi.create()
}
