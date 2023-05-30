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

    private val disposable = CompositeDisposable()

    fun fetchEntries(entityType: EntityType, entityCategory: EntityCategory) {
        when (entityType) {
            EntityType.DOCUMENTARY -> disposable.add(db.documentaryDao().getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.UNSUCCESSFUL }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.SUCCESSFUL(entries) })

            EntityType.BOOK -> disposable.add(db.bookDao().getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.UNSUCCESSFUL }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.SUCCESSFUL(entries) })

            EntityType.MOVIE -> disposable.add(db.movieDao().getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.UNSUCCESSFUL }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.SUCCESSFUL(entries) })

            EntityType.GAME -> disposable.add(db.gameDao().getAll(entityCategory)
                .map { it.map { Entry("Title: ${it.name}\n", it.posterUrl) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.UNSUCCESSFUL }
                .subscribe { entries -> _fetchEvent.value = FetchEvent.SUCCESSFUL(entries) })
        }
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    fun saveMovie(movie: Movie) {
        disposable.add(
            db.movieDao().insert(movie).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.UNSUCCESSFUL }
                .subscribe { _saveEvent.value = SaveEvent.SUCCESSFUL }
        )
    }

    fun saveBook(book: Book) {
        disposable.add(
            db.bookDao().insert(book).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.UNSUCCESSFUL }
                .subscribe { _saveEvent.value = SaveEvent.SUCCESSFUL }
        )
    }

    fun saveDocumentary(documentary: Documentary) {
        disposable.add(
            db.documentaryDao().insert(documentary).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.UNSUCCESSFUL }
                .subscribe { _saveEvent.value = SaveEvent.SUCCESSFUL }
        )
    }

    fun saveGame(game: Game) {
        disposable.add(
            db.gameDao().insert(game).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError { _saveEvent.value = SaveEvent.UNSUCCESSFUL }
                .subscribe { _saveEvent.value = SaveEvent.SUCCESSFUL }
        )
    }

    sealed class SaveEvent {
        object SUCCESSFUL : SaveEvent()
        object UNSUCCESSFUL : SaveEvent()
    }

    sealed class FetchEvent {
        data class SUCCESSFUL(val entries: List<Entry>) : FetchEvent()
        object UNSUCCESSFUL : FetchEvent()
    }
}
