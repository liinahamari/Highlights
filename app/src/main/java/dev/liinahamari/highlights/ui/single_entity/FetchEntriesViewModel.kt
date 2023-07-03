package dev.liinahamari.highlights.ui.single_entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.RoomDatabase
import dev.liinahamari.highlights.db.EntriesDatabase
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class FetchEntriesViewModel @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val documentaryDao: DocumentaryDao,
    private val gameDao: GameDao
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _fetchEvent = SingleLiveEvent<FetchEvent>()
    val fetchEvent: LiveData<FetchEvent> get() = _fetchEvent

    fun fetchEntries(entityType: EntityType, entityCategory: EntityCategory) {
        when (entityType) {
            EntityType.DOCUMENTARY -> documentaryDao.getAll(entityCategory)
                .map { it.map { Entry(it.name, "Title: ${it.name}\nCountries: ${it.countryCodes}", it.posterUrl, Documentary::class.java) } }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribeUi { entries -> _fetchEvent.value = FetchEvent.Success(entries) }

            EntityType.BOOK -> bookDao.getAll(entityCategory)
                .map { it.map { Entry(it.name, "Title: ${it.name}\nCountries: ${it.countryCodes.contentToString()}\nGenres: ${it.genres}", it.posterUrl, Book::class.java) } }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribeUi { entries -> _fetchEvent.value = FetchEvent.Success(entries) }

            EntityType.MOVIE -> movieDao.getAll(entityCategory)
                .map { it.map { Entry(it.name, "Title: ${it.name}\nCountries: ${it.countryCodes.contentToString()}\n" +
                        "Genres: ${it.genres}", it.posterUrl, Movie::class.java) } }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribeUi { entries -> _fetchEvent.value = FetchEvent.Success(entries) }

            EntityType.GAME -> gameDao.getAll(entityCategory)
                .map { it.map { Entry(it.name, "Title: ${it.name}\nCountries: ${it.countryCodes.contentToString()}\n" +
                        "Genres: ${it.genres}", it.posterUrl, Game::class.java) } }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchEvent.value = FetchEvent.Failure }
                .subscribeUi { entries -> _fetchEvent.value = FetchEvent.Success(entries) }
        }
    }

    override fun onCleared() = disposeSubscriptions()

    sealed class FetchEvent {
        data class Success(val entries: List<Entry>) : FetchEvent()
        object Failure : FetchEvent()
    }
}
