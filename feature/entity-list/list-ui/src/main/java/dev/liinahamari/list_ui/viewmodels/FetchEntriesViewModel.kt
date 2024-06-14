package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.entities.EntryUi
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.api.domain.usecases.get.GetAllBooksResult
import dev.liinahamari.api.domain.usecases.get.GetAllDocumentariesResult
import dev.liinahamari.api.domain.usecases.get.GetAllGamesResult
import dev.liinahamari.api.domain.usecases.get.GetAllMoviesResult
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.core.RxSubscriptionDelegateImpl
import dev.liinahamari.core.RxSubscriptionsDelegate
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.list_ui.single_entity.EntityType
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

    private val _filterEvent = SingleLiveEvent<FilterEvent>()
    val filterEvent: LiveData<FilterEvent> get() = _filterEvent

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
                .subscribeUi { result ->
                    when (result) {
                        is GetAllDocumentariesResult.Success -> _fetchAllEvent.value = FetchAllEvent.Success(result.data)
                        is GetAllDocumentariesResult.EmptyList -> _fetchAllEvent.value = FetchAllEvent.Empty
                        is GetAllDocumentariesResult.Error -> _fetchAllEvent.value = FetchAllEvent.Failure
                    }
                }

            EntityType.BOOK -> getBooksUseCase.getAllBooks(entityCategory)
                .subscribeUi { result ->
                    when (result) {
                        is GetAllBooksResult.Success -> _fetchAllEvent.value = FetchAllEvent.Success(result.data)
                        is GetAllBooksResult.EmptyList -> _fetchAllEvent.value = FetchAllEvent.Empty
                        is GetAllBooksResult.Error -> _fetchAllEvent.value = FetchAllEvent.Failure
                    }
                }

            EntityType.MOVIE -> getMoviesUseCase.getAllMovies(entityCategory)
                .subscribeUi { result ->
                    when (result) {
                        is GetAllMoviesResult.Success -> _fetchAllEvent.value = FetchAllEvent.Success(result.data)
                        is GetAllMoviesResult.EmptyList -> _fetchAllEvent.value = FetchAllEvent.Empty
                        is GetAllMoviesResult.Error -> _fetchAllEvent.value = FetchAllEvent.Failure
                    }
                }
            EntityType.GAME -> getGamesUseCase.getAllGames(entityCategory)
                .subscribeUi { result ->
                    when (result) {
                        is GetAllGamesResult.Success -> _fetchAllEvent.value = FetchAllEvent.Success(result.data)
                        is GetAllGamesResult.EmptyList -> _fetchAllEvent.value = FetchAllEvent.Empty
                        is GetAllGamesResult.Error -> _fetchAllEvent.value = FetchAllEvent.Failure
                    }
                }
        }
    }

    fun filter(entityType: EntityType, entityCategory: Category, countryCode: String) {
        when (entityType) {
            EntityType.DOCUMENTARY -> getDocumentariesUseCase.filter(entityCategory, countryCode)
                .map {
                    it.map {
                        EntryUi(
                            it.id,
                            title = it.name,
                            description = it.description,
                            genres = "",
                            countries = it.countryCodes.map { Locale("", it).displayCountry },
                            url = it.posterUrl,
                            year = it.year,
                            clazz = Documentary::class.java
                        )
                    }
                }
                .doOnError { _filterEvent.value = FilterEvent.Failure }
                .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }

            EntityType.BOOK -> getBooksUseCase.filter(entityCategory, countryCode)
                .map {
                    it.map {
                        EntryUi(
                            it.id,
                            title = it.name,
                            genres = it.genres.toString(),
                            description = it.description,
                            countries = it.countryCodes.map { Locale("", it).displayCountry },
                            url = it.posterUrl,
                            year = it.year,
                            clazz = Book::class.java
                        )
                    }
                }
                .doOnError { _filterEvent.value = FilterEvent.Failure }
                .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }

            EntityType.MOVIE -> getMoviesUseCase.filter(entityCategory, countryCode)
                .map {
                    it.map {
                        EntryUi(
                            it.id,
                            title = it.name,
                            genres = it.genres.toString(),
                            description = it.description,
                            countries = it.countryCodes.map { Locale("", it).displayCountry },
                            url = it.posterUrl,
                            year = it.year,
                            clazz = Movie::class.java
                        )
                    }
                }
                .doOnError { _filterEvent.value = FilterEvent.Failure }
                .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }

            EntityType.GAME -> getGamesUseCase.filter(entityCategory, countryCode)
                .map {
                    it.map {
                        EntryUi(
                            it.id,
                            title = it.name,
                            genres = it.genres.toString(),
                            description = it.description,
                            countries = it.countryCodes.map { Locale("", it).displayCountry },
                            url = it.posterUrl,
                            year = it.year,
                            clazz = Game::class.java
                        )
                    }
                }
                .doOnError { _filterEvent.value = FilterEvent.Failure }
                .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }
        }
    }

    override fun onCleared() = disposeSubscriptions()

    sealed class FetchAllEvent {
        data class Success(val entries: List<EntryUi>) : FetchAllEvent()
        object Failure : FetchAllEvent()
        object Empty : FetchAllEvent()
    }

    sealed class FilterEvent {
        data class Success(val entries: List<EntryUi>) : FilterEvent()
        object Failure : FilterEvent()
    }

    sealed class FindEntityEvent {
        data class Success(val entry: Any) : FindEntityEvent()
        object Failure : FindEntityEvent()
    }
}
