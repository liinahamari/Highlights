package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.usecases.delete.DeleteBookUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteGameUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteMovieUseCase
import dev.liinahamari.core.RxSubscriptionDelegateImpl
import dev.liinahamari.core.RxSubscriptionsDelegate
import dev.liinahamari.core.SingleLiveEvent
import javax.inject.Inject

class DeleteEntryViewModel @Inject constructor(
    private val deleteBookUseCase: DeleteBookUseCase,
    private val deleteGameUseCase: DeleteGameUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val deleteDocumentaryUseCase: DeleteDocumentaryUseCase,
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _deleteEvent = SingleLiveEvent<DeleteEvent>()
    val deleteEvent: LiveData<DeleteEvent> get() = _deleteEvent

    fun deleteBook(id: Long, position: Int) {
        deleteBookUseCase.deleteBook(id)
            .doOnError { _deleteEvent.value = DeleteEvent.Failure }
            .subscribeUi { _deleteEvent.value = DeleteEvent.Success(position) }
    }

    fun deleteMovie(id: Long, position: Int) {
        deleteMovieUseCase.deleteMovie(id)
            .doOnError { _deleteEvent.value = DeleteEvent.Failure }
            .subscribeUi { _deleteEvent.value = DeleteEvent.Success(position) }
    }

    fun deleteDocumentary(id: Long, position: Int) {
        deleteDocumentaryUseCase.deleteDocumentary(id)
            .doOnError { _deleteEvent.value = DeleteEvent.Failure }
            .subscribeUi { _deleteEvent.value = DeleteEvent.Success(position) }
    }

    fun deleteGame(id: Long, position: Int) {
        deleteGameUseCase.deleteGame(id)
            .doOnError { _deleteEvent.value = DeleteEvent.Failure }
            .subscribeUi { _deleteEvent.value = DeleteEvent.Success(position) }
    }

    override fun onCleared() = disposeSubscriptions()
}

sealed class DeleteEvent {
    data class Success(val position: Int) : DeleteEvent()
    object Failure : DeleteEvent()
}

