package dev.liinahamari.movies_suggestions.api

interface MovieSuggestionsApi {
    val searchMovieUseCase: SearchMovieUseCase

    companion object
}
