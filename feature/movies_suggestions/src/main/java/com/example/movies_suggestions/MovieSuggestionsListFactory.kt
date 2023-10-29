package com.example.movies_suggestions

import dev.liinahamari.movies_suggestions.api.MovieSuggestionsApi
import dev.liinahamari.movies_suggestions.impl.di.create

object MovieSuggestionsListFactory {
    @JvmStatic fun getApi() = MovieSuggestionsApi.create()
}
