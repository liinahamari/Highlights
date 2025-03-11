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
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class DeleteBookViewModel (private val deleteBookUseCase: DeleteBookUseCase) :
    DeleteEntryViewModel() {
    override fun delete(ids: Set<Long>) {
        Observable.fromIterable(ids)
            .flatMapCompletable(deleteBookUseCase::deleteBook)
            .doOnError { _bunchDeleteEvent.postValue(BunchDeleteEvent.Failure) }
            .doOnComplete { _bunchDeleteEvent.postValue(BunchDeleteEvent.Success) }
            .subscribeUi()
    }
}

class DeleteGameViewModel (private val deleteGameUseCase: DeleteGameUseCase) :
    DeleteEntryViewModel() {
    override fun delete(ids: Set<Long>) {
        Observable.fromIterable(ids)
            .flatMapCompletable(deleteGameUseCase::deleteGame)
            .doOnError { _bunchDeleteEvent.postValue(BunchDeleteEvent.Failure) }
            .doOnComplete { _bunchDeleteEvent.postValue(BunchDeleteEvent.Success) }
            .subscribeUi()
    }
}

class DeleteMovieViewModel (private val deleteMovieUseCase: DeleteMovieUseCase) :
    DeleteEntryViewModel() {
    override fun delete(ids: Set<Long>) {
        Observable.fromIterable(ids)
            .flatMapCompletable(deleteMovieUseCase::deleteMovie)
            .doOnError { _bunchDeleteEvent.postValue(BunchDeleteEvent.Failure) }
            .doOnComplete { _bunchDeleteEvent.postValue(BunchDeleteEvent.Success) }
            .subscribeUi()
    }
}

class DeleteShortViewModel (private val deleteShortsUseCase: DeleteShortUseCase) :
    DeleteEntryViewModel() {
    override fun delete(ids: Set<Long>) {
        Observable.fromIterable(ids)
            .flatMapCompletable(deleteShortsUseCase::deleteShort)
            .doOnError { _bunchDeleteEvent.postValue(BunchDeleteEvent.Failure) }
            .doOnComplete { _bunchDeleteEvent.postValue(BunchDeleteEvent.Success) }
            .subscribeUi()
    }
}

class DeleteDocumentaryViewModel (private val deleteDocumentaryUseCase: DeleteDocumentaryUseCase) :
    DeleteEntryViewModel() {
    override fun delete(ids: Set<Long>) {
        Observable.fromIterable(ids)
            .flatMapCompletable(deleteDocumentaryUseCase::deleteDocumentary)
            .doOnError { _bunchDeleteEvent.postValue(BunchDeleteEvent.Failure) }
            .doOnComplete { _bunchDeleteEvent.postValue(BunchDeleteEvent.Success) }
            .subscribeUi()
    }
}

open class DeleteEntryViewModel @Inject constructor() : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    protected val _bunchDeleteEvent = SingleLiveEvent<BunchDeleteEvent>()
    val bunchDeleteEvent: LiveData<BunchDeleteEvent> get() = _bunchDeleteEvent

    open fun delete(ids: Set<Long>) {}
    override fun onCleared() = disposeSubscriptions()
}

sealed class BunchDeleteEvent {
    data object Success : BunchDeleteEvent()
    data object Failure : BunchDeleteEvent()
}

