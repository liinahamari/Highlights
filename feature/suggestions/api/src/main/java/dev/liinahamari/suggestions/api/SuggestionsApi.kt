package dev.liinahamari.suggestions.api

interface SuggestionsApi {
    val searchMovieUseCase: SearchMovieUseCase
    val searchBookUseCase: SearchBookUseCase
    val searchGameUseCase: SearchGameUseCase

    companion object
}
