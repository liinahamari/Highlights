package dev.liinahamari.highlights.ui.single_entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.highlights.db.daos.Book
import dev.liinahamari.highlights.db.daos.BookDao
import dev.liinahamari.highlights.db.daos.Documentary
import dev.liinahamari.highlights.db.daos.DocumentaryDao
import dev.liinahamari.highlights.db.daos.Game
import dev.liinahamari.highlights.db.daos.GameDao
import dev.liinahamari.highlights.db.daos.Movie
import dev.liinahamari.highlights.db.daos.MovieDao
import dev.liinahamari.highlights.helper.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SaveEntryViewModel @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val documentaryDao: DocumentaryDao,
    private val gameDao: GameDao
) : ViewModel() {
    private val disposable = CompositeDisposable()

    private val _saveEvent = SingleLiveEvent<SaveEvent>()
    val saveEvent: LiveData<SaveEvent> get() = _saveEvent

    fun saveMovie(movie: Movie) {
        disposable.add(
            movieDao.insert(movie).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.Failure }
                .subscribe { _saveEvent.value = SaveEvent.Success }
        )
    }

    fun saveBook(book: Book) {
        disposable.add(
            bookDao.insert(book).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.Failure }
                .subscribe { _saveEvent.value = SaveEvent.Success }
        )
    }

    fun saveDocumentary(documentary: Documentary) {
        disposable.add(
            documentaryDao.insert(documentary).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.Failure }
                .subscribe { _saveEvent.value = SaveEvent.Success }
        )
    }

    fun saveGame(game: Game) {
        disposable.add(
            gameDao.insert(game).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.Failure }
                .subscribe { _saveEvent.value = SaveEvent.Success }
        )
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}

sealed class SaveEvent {
    object Success : SaveEvent()
    object Failure : SaveEvent()
}
