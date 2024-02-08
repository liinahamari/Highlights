package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.core.RxSubscriptionDelegateImpl
import dev.liinahamari.core.RxSubscriptionsDelegate
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.list_ui.single_entity.EntityType
import dev.liinahamari.list_ui.single_entity.Entry
import java.util.Locale
import javax.inject.Inject

class FetchEntriesViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase,
    private val getDocumentariesUseCase: GetDocumentariesUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getBooksUseCase: GetBooksUseCase
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _fetchAllEvent = SingleLiveEvent<FetchAllEvent>()
    val fetchAllEvent: LiveData<FetchAllEvent> get() = _fetchAllEvent

    private val _findEntityEvent = SingleLiveEvent<FindEntityEvent>()
    val findEntityEvent: LiveData<FindEntityEvent> get() = _findEntityEvent

    fun findEntry(entityType: EntityType, entityCategory: Category, id: Long) {
        when (entityType) {
            EntityType.DOCUMENTARY -> getDocumentariesUseCase.findById(entityCategory, id)
                .doOnError { _findEntityEvent.value = FindEntityEvent.Failure }
                .subscribeUi { entry -> _findEntityEvent.value = FindEntityEvent.Success(entry) }

            EntityType.BOOK -> getBooksUseCase.findById(entityCategory, id)
                .doOnError { _findEntityEvent.value = FindEntityEvent.Failure }
                .subscribeUi { entry -> _findEntityEvent.value = FindEntityEvent.Success(entry) }

            EntityType.MOVIE -> getMoviesUseCase.findById(entityCategory, id)
                .doOnError { _findEntityEvent.value = FindEntityEvent.Failure }
                .subscribeUi { entry -> _findEntityEvent.value = FindEntityEvent.Success(entry) }

            EntityType.GAME -> getGamesUseCase.findById(entityCategory, id)
                .doOnError { _findEntityEvent.value = FindEntityEvent.Failure }
                .subscribeUi { entry -> _findEntityEvent.value = FindEntityEvent.Success(entry) }
        }
    }

    fun fetchEntries(entityType: EntityType, entityCategory: Category) {
        when (entityType) {
            EntityType.DOCUMENTARY -> getDocumentariesUseCase.getAllDocumentaries(entityCategory)
                .map {
                    it.map {
                        Entry(
                            it.id,
                            title = it.name,
                            description = it.description,
                            genres = "",
                            countries = it.countryCodes.map { Locale("", it).displayCountry },
                            url = it.posterUrl,
                            clazz = Documentary::class.java
                        )
                    }
                }
                .doOnError { _fetchAllEvent.value = FetchAllEvent.Failure }
                .subscribeUi { entries -> _fetchAllEvent.value = FetchAllEvent.Success(entries) }

            EntityType.BOOK -> getBooksUseCase.getAllBooks(entityCategory)
                .map {
                    it.map {
                        Entry(
                            it.id,
                            title = it.name,
                            genres = it.genres.toString(),
                            description = it.description,
                            countries = it.countryCodes.map { Locale("", it).displayCountry },
                            url = it.posterUrl,
                            clazz = Book::class.java
                        )
                    }
                }
                .doOnError { _fetchAllEvent.value = FetchAllEvent.Failure }
                .subscribeUi { entries -> _fetchAllEvent.value = FetchAllEvent.Success(entries) }

            EntityType.MOVIE -> getMoviesUseCase.getAllMovies(entityCategory)
                .map {
                    it.map {
                        Entry(
                            it.id,
                            title = it.name,
                            genres = it.genres.toString(),
                            description = it.description,
                            countries = it.countryCodes.map { Locale("", it).displayCountry },
                            url = it.posterUrl,
                            clazz = Movie::class.java
                        )
                    }
                }
                .doOnError { _fetchAllEvent.value = FetchAllEvent.Failure }
                .subscribeUi { entries -> _fetchAllEvent.value = FetchAllEvent.Success(entries) }

            EntityType.GAME -> getGamesUseCase.getAllGames(entityCategory)
                .map {
                    it.map {
                        Entry(
                            it.id,
                            title = it.name,
                            genres = it.genres.toString(),
                            description = it.description,
                            countries = it.countryCodes.map { Locale("", it).displayCountry },
                            url = it.posterUrl,
                            clazz = Game::class.java
                        )
                    }
                }
                .doOnError { _fetchAllEvent.value = FetchAllEvent.Failure }
                .subscribeUi { entries -> _fetchAllEvent.value = FetchAllEvent.Success(entries) }
        }
    }

    override fun onCleared() = disposeSubscriptions()

    sealed class FetchAllEvent {
        data class Success(val entries: List<Entry>) : FetchAllEvent()
        object Failure : FetchAllEvent()
    }

    sealed class FindEntityEvent {
        data class Success(val entry: Any) : FindEntityEvent()
        object Failure : FindEntityEvent()
    }
}
