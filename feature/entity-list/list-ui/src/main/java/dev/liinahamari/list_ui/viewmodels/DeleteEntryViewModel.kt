package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.usecases.delete.DeleteBookUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteGameUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteMovieUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteShortUseCase
import dev.liinahamari.core.RxSubscriptionDelegateImpl
import dev.liinahamari.core.RxSubscriptionsDelegate
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.list_ui.single_entity.EntityType
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class DeleteEntryViewModel @Inject constructor(
    private val deleteBookUseCase: DeleteBookUseCase,
    private val deleteGameUseCase: DeleteGameUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val deleteShortUseCase: DeleteShortUseCase,
    private val deleteDocumentaryUseCase: DeleteDocumentaryUseCase,
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _bunchDeleteEvent = SingleLiveEvent<BunchDeleteEvent>()
    val bunchDeleteEvent: LiveData<BunchDeleteEvent> get() = _bunchDeleteEvent

    override fun onCleared() = disposeSubscriptions()

    fun delete(ids: Set<Long>, entityType: EntityType) = Observable.fromIterable(ids).flatMapCompletable {
        return@flatMapCompletable when (entityType) {
            EntityType.BOOK -> deleteBookUseCase.deleteBook(it)
            EntityType.GAME -> deleteGameUseCase.deleteGame(it)
            EntityType.DOCUMENTARY -> deleteDocumentaryUseCase.deleteDocumentary(it)
            EntityType.MOVIE -> deleteMovieUseCase.deleteMovie(it)
            EntityType.SHORT -> deleteShortUseCase.deleteShort(it)
        }
    }
        .doOnError { _bunchDeleteEvent.postValue(BunchDeleteEvent.Failure) }
        .doOnComplete { _bunchDeleteEvent.postValue(BunchDeleteEvent.Success) }
        .subscribeUi()
}

sealed class BunchDeleteEvent {
    data object Success : BunchDeleteEvent()
    data object Failure : BunchDeleteEvent()
}

