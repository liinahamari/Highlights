package dev.liinahamari.highlights.ui.single_entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.highlights.db.daos.BookDao
import dev.liinahamari.highlights.db.daos.DocumentaryDao
import dev.liinahamari.highlights.db.daos.GameDao
import dev.liinahamari.highlights.db.daos.MovieDao
import dev.liinahamari.highlights.helper.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DeleteEntryViewModel @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val documentaryDao: DocumentaryDao,
    private val gameDao: GameDao
) : ViewModel() {
    private val disposable = CompositeDisposable()

    private val _deleteEvent = SingleLiveEvent<DeleteEvent>()
    val deleteEvent: LiveData<DeleteEvent> get() = _deleteEvent

    fun deleteBook(id: String, position: Int) {
        disposable.add(
            bookDao.delete(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _deleteEvent.value = DeleteEvent.Failure }
                .subscribe { _deleteEvent.value = DeleteEvent.Success(position) }
        )
    }

    fun deleteMovie(id: String, position: Int) {
        disposable.add(
            movieDao.delete(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _deleteEvent.value = DeleteEvent.Failure }
                .subscribe { _deleteEvent.value = DeleteEvent.Success(position) }
        )
    }

    fun deleteDocumentary(id: String, position: Int) {
        disposable.add(
            documentaryDao.delete(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _deleteEvent.value = DeleteEvent.Failure }
                .subscribe { _deleteEvent.value = DeleteEvent.Success(position) }
        )
    }

    fun deleteGame(id: String, position: Int) {
        disposable.add(
            gameDao.delete(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _deleteEvent.value = DeleteEvent.Failure }
                .subscribe { _deleteEvent.value = DeleteEvent.Success(position) }
        )
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}

sealed class DeleteEvent {
    data class Success(val position: Int) : DeleteEvent()
    object Failure : DeleteEvent()
}

