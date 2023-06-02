package dev.liinahamari.highlights.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.highlights.SingleLiveEvent
import dev.liinahamari.highlights.db.EntriesDatabase
import dev.liinahamari.highlights.db.daos.Book
import dev.liinahamari.highlights.db.daos.Documentary
import dev.liinahamari.highlights.db.daos.Game
import dev.liinahamari.highlights.db.daos.Movie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class EntriesViewModel(
    private val db: EntriesDatabase
) : ViewModel() {
    private val _saveEvent = SingleLiveEvent<SaveEvent>()
    val saveEvent: LiveData<SaveEvent> get() = _saveEvent

    private val _fetchEvent = SingleLiveEvent<FetchEvent>()
    val fetchEvent: LiveData<FetchEvent> get() = _fetchEvent

    //todo delegate
    private val disposable = CompositeDisposable()

    fun fetchEntries(entityType: EntityType, entityCategory: EntityCategory) {
        when (entityType) {
            EntityType.DOCUMENTARY -> disposable.add(db.documentaryDao().getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.Success(entries) })

            EntityType.BOOK -> disposable.add(db.bookDao().getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.Success(entries) })

            EntityType.MOVIE -> disposable.add(db.movieDao().getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.Success(entries) })

            EntityType.GAME -> disposable.add(db.gameDao().getAll(entityCategory)
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
            db.movieDao().insert(movie).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.Failure }
                .subscribe { _saveEvent.value = SaveEvent.Success }
        )
    }

    fun saveBook(book: Book) {
        disposable.add(
            db.bookDao().insert(book).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.Failure }
                .subscribe { _saveEvent.value = SaveEvent.Success }
        )
    }

    fun saveDocumentary(documentary: Documentary) {
        disposable.add(
            db.documentaryDao().insert(documentary).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.Failure }
                .subscribe { _saveEvent.value = SaveEvent.Success }
        )
    }

    fun saveGame(game: Game) {
        disposable.add(
            db.gameDao().insert(game).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
