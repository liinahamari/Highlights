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
import dev.liinahamari.highlights.helper.RxSubscriptionDelegateImpl
import dev.liinahamari.highlights.helper.RxSubscriptionsDelegate
import dev.liinahamari.highlights.helper.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import javax.inject.Inject

class SaveEntryViewModel @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val documentaryDao: DocumentaryDao,
    private val gameDao: GameDao
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _saveEvent = SingleLiveEvent<SaveEvent>()
    val saveEvent: LiveData<SaveEvent> get() = _saveEvent

    fun saveMovie(movie: Movie) {
        movieDao.insert(movie)
            .observeOn(mainThread())
            .doOnError { _saveEvent.value = SaveEvent.Failure }
            .subscribeUi { _saveEvent.value = SaveEvent.Success }
    }

    fun saveBook(book: Book) {
        bookDao.insert(book)
            .observeOn(mainThread())
            .doOnError { _saveEvent.value = SaveEvent.Failure }
            .subscribeUi { _saveEvent.value = SaveEvent.Success }
    }

    fun saveDocumentary(documentary: Documentary) {
        documentaryDao.insert(documentary)
            .observeOn(mainThread())
            .doOnError { _saveEvent.value = SaveEvent.Failure }
            .subscribeUi { _saveEvent.value = SaveEvent.Success }
    }

    fun saveGame(game: Game) {
        gameDao.insert(game)
            .observeOn(mainThread())
            .doOnError { _saveEvent.value = SaveEvent.Failure }
            .subscribeUi { _saveEvent.value = SaveEvent.Success }
    }

    override fun onCleared() = disposeSubscriptions()
}

sealed class SaveEvent {
    object Success : SaveEvent()
    object Failure : SaveEvent()
}
