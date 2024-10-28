package dev.liinahamari.api

import android.app.Application
import dev.liinahamari.api.domain.repo.PreferencesRepo
import dev.liinahamari.api.domain.repo.ShakeCounterRepo
import dev.liinahamari.api.domain.usecases.CacheCountriesUseCase
import dev.liinahamari.api.domain.usecases.ComposeShareMessageUseCase
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.api.domain.usecases.RestoreDatabaseUseCase
import dev.liinahamari.api.domain.usecases.SaveDatabaseUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteBookUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteGameUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteMovieUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteShortUseCase
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.api.domain.usecases.get.GetShortsUseCase
import dev.liinahamari.api.domain.usecases.save.SaveBookUseCase
import dev.liinahamari.api.domain.usecases.save.SaveDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.save.SaveGameUseCase
import dev.liinahamari.api.domain.usecases.save.SaveMovieUseCase
import dev.liinahamari.api.domain.usecases.save.SaveShortsUseCase

interface EntityListApi {
    val getBooksUseCase: GetBooksUseCase
    val getDocumentariesUseCase: GetDocumentariesUseCase
    val getGamesUseCase: GetGamesUseCase
    val getShortsUseCase: GetShortsUseCase
    val getMoviesUseCase: GetMoviesUseCase

    val saveGameUseCase: SaveGameUseCase
    val saveDocumentaryUseCase: SaveDocumentaryUseCase
    val saveShortsUseCase: SaveShortsUseCase
    val saveBookUseCase: SaveBookUseCase
    val saveMovieUseCase: SaveMovieUseCase

    val deleteBookUseCase: DeleteBookUseCase
    val deleteShortsUseCase: DeleteShortUseCase
    val deleteMovieUseCase: DeleteMovieUseCase
    val deleteGameUseCase: DeleteGameUseCase
    val deleteDocumentaryUseCase: DeleteDocumentaryUseCase

    val saveDatabaseUseCase: SaveDatabaseUseCase
    val restoreDatabaseUseCase: RestoreDatabaseUseCase

    val databaseCountersUseCase: DatabaseCountersUseCase
    val shareMessageUseCase: ComposeShareMessageUseCase
    val cacheCountriesUseCase: CacheCountriesUseCase
    val shakeCounterRepo: ShakeCounterRepo
    val prefRepo: PreferencesRepo

    val app: Application

    companion object
}
