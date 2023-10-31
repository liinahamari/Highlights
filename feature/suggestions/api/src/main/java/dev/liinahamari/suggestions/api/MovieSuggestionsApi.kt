package dev.liinahamari.suggestions.api

interface MovieSuggestionsApi {
    val searchMovieUseCase: SearchMovieUseCase

    companion object
}
