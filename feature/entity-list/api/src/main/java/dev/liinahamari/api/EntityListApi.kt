package dev.liinahamari.api

import dev.liinahamari.api.domain.usecases.delete.DeleteBookUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteGameUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteMovieUseCase
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.api.domain.usecases.save.SaveBookUseCase
import dev.liinahamari.api.domain.usecases.save.SaveDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.save.SaveGameUseCase
import dev.liinahamari.api.domain.usecases.save.SaveMovieUseCase

interface EntityListApi {
    val getBooksUseCase: GetBooksUseCase
    val getDocumentariesUseCase: GetDocumentariesUseCase
    val getGamesUseCase: GetGamesUseCase
    val getMoviesUseCase: GetMoviesUseCase

    val saveBookUseCase: SaveGameUseCase
    val saveDocumentaryUseCase: SaveDocumentaryUseCase
    val saveGameUseCase: SaveBookUseCase
    val saveMovieUseCase: SaveMovieUseCase

    val deleteBookUseCase: DeleteBookUseCase
    val deleteMovieUseCase: DeleteMovieUseCase
    val deleteGameUseCase: DeleteGameUseCase
    val deleteDocumentaryUseCase: DeleteDocumentaryUseCase

    companion object
}