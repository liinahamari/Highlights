package dev.liinahamari.api

import dev.liinahamari.api.domain.usecases.GetAllBooksUseCase
import dev.liinahamari.api.domain.usecases.GetAllDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.GetAllGamesUseCase
import dev.liinahamari.api.domain.usecases.GetAllMoviesUseCase

interface EntityListApi {
    fun getAllBooksUseCase(): GetAllBooksUseCase
    fun getAllDocumentariesUseCase(): GetAllDocumentariesUseCase
    fun getAllGamesUseCase(): GetAllGamesUseCase
    fun getAllMoviesUseCase(): GetAllMoviesUseCase

    companion object
}
