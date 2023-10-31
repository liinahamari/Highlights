package com.example.suggestions

import dev.liinahamari.suggestions.api.SuggestionsApi
import dev.liinahamari.suggestions.impl.di.create

object MovieSuggestionsListFactory {
    @JvmStatic fun getApi() = SuggestionsApi.create()
}
