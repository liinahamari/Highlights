package dev.liinahamari.suggestions.api

import dev.liinahamari.suggestions.api.usecases.GetMovieGenresUseCase
import dev.liinahamari.suggestions.api.usecases.SearchBookUseCase
import dev.liinahamari.suggestions.api.usecases.SearchGameUseCase
import dev.liinahamari.suggestions.api.usecases.SearchMovieUseCase

interface SuggestionsApi {
    val getMovieGenresUseCase: GetMovieGenresUseCase
    val searchMovieUseCase: SearchMovieUseCase
    val searchBookUseCase: SearchBookUseCase
    val searchGameUseCase: SearchGameUseCase

    companion object
}
