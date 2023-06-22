package dev.liinahamari.highlights.ui.single_entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.highlights.db.daos.BookDao
import dev.liinahamari.highlights.db.daos.DocumentaryDao
import dev.liinahamari.highlights.db.daos.GameDao
import dev.liinahamari.highlights.db.daos.MovieDao
import dev.liinahamari.highlights.helper.RxSubscriptionDelegateImpl
import dev.liinahamari.highlights.helper.RxSubscriptionsDelegate
import dev.liinahamari.highlights.helper.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class DeleteEntryViewModel @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val documentaryDao: DocumentaryDao,
    private val gameDao: GameDao
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _deleteEvent = SingleLiveEvent<DeleteEvent>()
    val deleteEvent: LiveData<DeleteEvent> get() = _deleteEvent

    fun deleteBook(id: String, position: Int) {
        bookDao.delete(id)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _deleteEvent.value = DeleteEvent.Failure }
            .subscribeUi { _deleteEvent.value = DeleteEvent.Success(position) }
    }

    fun deleteMovie(id: String, position: Int) {
        movieDao.delete(id)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _deleteEvent.value = DeleteEvent.Failure }
            .subscribeUi { _deleteEvent.value = DeleteEvent.Success(position) }
    }

    fun deleteDocumentary(id: String, position: Int) {
        documentaryDao.delete(id)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _deleteEvent.value = DeleteEvent.Failure }
            .subscribeUi { _deleteEvent.value = DeleteEvent.Success(position) }
    }

    fun deleteGame(id: String, position: Int) {
        gameDao.delete(id)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _deleteEvent.value = DeleteEvent.Failure }
            .subscribeUi { _deleteEvent.value = DeleteEvent.Success(position) }
    }

    override fun onCleared() = disposeSubscriptions()
}

sealed class DeleteEvent {
    data class Success(val position: Int) : DeleteEvent()
    object Failure : DeleteEvent()
}

