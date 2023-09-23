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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class FetchEntriesViewModel @Inject constructor(
    private val bookDao: BookDao,
    private val movieDao: MovieDao,
    private val documentaryDao: DocumentaryDao,
    private val gameDao: GameDao
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _fetchAllEvent = SingleLiveEvent<FetchAllEvent>()
    val fetchAllEvent: LiveData<FetchAllEvent> get() = _fetchAllEvent

    private val _fetchSingleEvent = SingleLiveEvent<FetchSingleEvent>()
    val fetchSingleEvent: LiveData<FetchSingleEvent> get() = _fetchSingleEvent

    fun fetchEntry(entityType: EntityType, entityCategory: EntityCategory, id: String) {
        when (entityType) {
            EntityType.DOCUMENTARY -> documentaryDao.findByName(entityCategory, id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchSingleEvent.value = FetchSingleEvent.Failure }
                .subscribeUi { entry -> _fetchSingleEvent.value = FetchSingleEvent.Success(entry) }

            EntityType.BOOK -> bookDao.findByName(entityCategory, id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchSingleEvent.value = FetchSingleEvent.Failure }
                .subscribeUi { entry -> _fetchSingleEvent.value = FetchSingleEvent.Success(entry) }

            EntityType.MOVIE -> movieDao.findByName(entityCategory, id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchSingleEvent.value = FetchSingleEvent.Failure }
                .subscribeUi { entry -> _fetchSingleEvent.value = FetchSingleEvent.Success(entry) }

            EntityType.GAME -> gameDao.findByName(entityCategory, id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchSingleEvent.value = FetchSingleEvent.Failure }
                .subscribeUi { entry -> _fetchSingleEvent.value = FetchSingleEvent.Success(entry) }
        }
    }

    fun fetchEntries(entityType: EntityType, entityCategory: EntityCategory) {
        when (entityType) {
            EntityType.DOCUMENTARY -> documentaryDao.getAll(entityCategory)
                .map { it.map { Entry(it.name, "Title: ${it.name}\nCountries: ${it.countryCodes}", it.posterUrl, Documentary::class.java) } }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchAllEvent.value = FetchAllEvent.Failure }
                .subscribeUi { entries -> _fetchAllEvent.value = FetchAllEvent.Success(entries) }

            EntityType.BOOK -> bookDao.getAll(entityCategory)
                .map { it.map { Entry(it.name, "Title: ${it.name}\nCountries: ${it.countryCodes.contentToString()}\nGenres: ${it.genres}", it.posterUrl, Book::class.java) } }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchAllEvent.value = FetchAllEvent.Failure }
                .subscribeUi { entries -> _fetchAllEvent.value = FetchAllEvent.Success(entries) }

            EntityType.MOVIE -> movieDao.getAll(entityCategory)
                .map { it.map { Entry(it.name, "Title: ${it.name}\nCountries: ${it.countryCodes.contentToString()}\n" +
                        "Genres: ${it.genres}", it.posterUrl, Movie::class.java) } }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchAllEvent.value = FetchAllEvent.Failure }
                .subscribeUi { entries -> _fetchAllEvent.value = FetchAllEvent.Success(entries) }

            EntityType.GAME -> gameDao.getAll(entityCategory)
                .map { it.map { Entry(it.name, "Title: ${it.name}\nCountries: ${it.countryCodes.contentToString()}\n" +
                        "Genres: ${it.genres}", it.posterUrl, Game::class.java) } }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _fetchAllEvent.value = FetchAllEvent.Failure }
                .subscribeUi { entries -> _fetchAllEvent.value = FetchAllEvent.Success(entries) }
        }
    }

    override fun onCleared() = disposeSubscriptions()

    sealed class FetchAllEvent {
        data class Success(val entries: List<Entry>) : FetchAllEvent()
        object Failure : FetchAllEvent()
    }
    sealed class FetchSingleEvent {
        data class Success(val entry: Any) : FetchSingleEvent()
        object Failure : FetchSingleEvent()
    }
}
