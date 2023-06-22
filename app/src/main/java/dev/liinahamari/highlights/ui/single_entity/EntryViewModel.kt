package dev.liinahamari.highlights.ui.single_entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.highlights.helper.SingleLiveEvent
import dev.liinahamari.highlights.db.daos.Book
import dev.liinahamari.highlights.db.daos.BookDao
import dev.liinahamari.highlights.db.daos.Documentary
import dev.liinahamari.highlights.db.daos.DocumentaryDao
import dev.liinahamari.highlights.db.daos.Game
import dev.liinahamari.highlights.db.daos.GameDao
import dev.liinahamari.highlights.db.daos.Movie
import dev.liinahamari.highlights.db.daos.MovieDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EntryViewModel @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val documentaryDao: DocumentaryDao,
    private val gameDao: GameDao
) : ViewModel() {
    private val _getBook = SingleLiveEvent<Book>()
    val getBook: LiveData<Book> get() = _getBook
    private val _saveEvent = SingleLiveEvent<SaveEvent>()
    val saveEvent: LiveData<SaveEvent> get() = _saveEvent

    private val _fetchEvent = SingleLiveEvent<FetchEvent>()
    val fetchEvent: LiveData<FetchEvent> get() = _fetchEvent

    //todo delegate
    private val disposable = CompositeDisposable()

    fun fetchEntries(entityType: EntityType, entityCategory: EntityCategory) {
        when (entityType) {
            EntityType.DOCUMENTARY -> disposable.add(documentaryDao.getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.Success(entries) })

            EntityType.BOOK -> disposable.add(bookDao.getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.Success(entries) })

            EntityType.MOVIE -> disposable.add(movieDao.getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.Success(entries) })

            EntityType.GAME -> disposable.add(gameDao.getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.Success(entries) })
        }
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

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

    sealed class SaveEvent {
        object Success : SaveEvent()
        object Failure : SaveEvent()
    }

    sealed class FetchEvent {
        data class Success(val entries: List<Entry>) : FetchEvent()
        object Failure : FetchEvent()
    }
}
