package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.api.domain.entities.Short
import dev.liinahamari.api.domain.usecases.save.SaveBookUseCase
import dev.liinahamari.api.domain.usecases.save.SaveDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.save.SaveGameUseCase
import dev.liinahamari.api.domain.usecases.save.SaveMovieUseCase
import dev.liinahamari.api.domain.usecases.save.SaveShortsUseCase
import dev.liinahamari.core.RxSubscriptionDelegateImpl
import dev.liinahamari.core.RxSubscriptionsDelegate
import javax.inject.Inject

class SaveEntryViewModel @Inject constructor(
    private val saveGameUseCase: SaveGameUseCase,
    private val saveDocumentaryUseCase: SaveDocumentaryUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
    private val saveBookUseCase: SaveBookUseCase,
    private val saveShortsUseCase: SaveShortsUseCase
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _saveEvent = MutableLiveData<SaveEvent>()
    val saveEvent: LiveData<SaveEvent> get() = _saveEvent

    fun saveMovie(movie: Movie) {
        saveMovieUseCase.saveMovies(movie)
            .doOnError { _saveEvent.value = SaveEvent.Failure }
            .subscribeUi { _saveEvent.value = SaveEvent.Success }
    }

    fun saveBook(book: Book) {
        saveBookUseCase.saveBooks(book)
            .doOnError { _saveEvent.value = SaveEvent.Failure }
            .subscribeUi { _saveEvent.value = SaveEvent.Success }
    }

    fun saveDocumentary(documentary: Documentary) {
        saveDocumentaryUseCase.saveDocumentaries(documentary)
            .doOnError { _saveEvent.value = SaveEvent.Failure }
            .subscribeUi { _saveEvent.value = SaveEvent.Success }
    }

    fun saveShort(short: Short) {
        saveShortsUseCase.saveShorts(short)
            .doOnError { _saveEvent.value = SaveEvent.Failure }
            .subscribeUi { _saveEvent.value = SaveEvent.Success }
    }

    fun saveGame(game: Game) {
        saveGameUseCase.saveGames(game)
            .doOnError { _saveEvent.value = SaveEvent.Failure }
            .subscribeUi { _saveEvent.value = SaveEvent.Success }
    }

    override fun onCleared() = disposeSubscriptions()
}

sealed class SaveEvent {
    object Success : SaveEvent()
    object Failure : SaveEvent()
}
