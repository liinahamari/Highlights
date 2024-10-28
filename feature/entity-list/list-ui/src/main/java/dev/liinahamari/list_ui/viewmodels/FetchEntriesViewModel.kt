package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Country
import dev.liinahamari.api.domain.entities.EntryUi
import dev.liinahamari.api.domain.entities.toBookUi
import dev.liinahamari.api.domain.entities.toDocumentaryUi
import dev.liinahamari.api.domain.entities.toMovieUi
import dev.liinahamari.api.domain.entities.toShortUi
import dev.liinahamari.api.domain.usecases.get.GetAllBooksResult
import dev.liinahamari.api.domain.usecases.get.GetAllDocumentariesResult
import dev.liinahamari.api.domain.usecases.get.GetAllGamesResult
import dev.liinahamari.api.domain.usecases.get.GetAllMoviesResult
import dev.liinahamari.api.domain.usecases.get.GetAllShortsResult
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.api.domain.usecases.get.GetShortsUseCase
import dev.liinahamari.core.RxSubscriptionDelegateImpl
import dev.liinahamari.core.RxSubscriptionsDelegate
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.list_ui.single_entity.EntityType
import javax.inject.Inject

class FetchEntriesViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase,
    private val getDocumentariesUseCase: GetDocumentariesUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getSortsUseCase: GetShortsUseCase,
    private val getBooksUseCase: GetBooksUseCase
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _fetchAllEvent = SingleLiveEvent<FetchAllEvent>()
    val fetchAllEvent: LiveData<FetchAllEvent> get() = _fetchAllEvent

    private val _filterEvent = SingleLiveEvent<FilterEvent>()
    val filterEvent: LiveData<FilterEvent> get() = _filterEvent

    private val _findEntityEvent = SingleLiveEvent<FindEntityEvent>()
    val findEntityEvent: LiveData<FindEntityEvent> get() = _findEntityEvent

    fun fetchEntries(entityType: EntityType, entityCategory: Category) {
        when (entityType) {
            EntityType.DOCUMENTARY -> getDocumentariesUseCase.getAllDocumentaries(entityCategory)
                .subscribeUi { result ->
                    when (result) {
                        is GetAllDocumentariesResult.Success -> _fetchAllEvent.value =
                            FetchAllEvent.Success(result.data)

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

            EntityType.SHORT -> getSortsUseCase.getAllShorts(entityCategory)
                .subscribeUi { result ->
                    when (result) {
                        is GetAllShortsResult.Success -> _fetchAllEvent.value = FetchAllEvent.Success(result.data)
                        is GetAllShortsResult.EmptyList -> _fetchAllEvent.value = FetchAllEvent.Empty
                        is GetAllShortsResult.Error -> _fetchAllEvent.value = FetchAllEvent.Failure
                    }
                }
        }
    }

    fun filter(entityType: EntityType, entityCategory: Category, country: Country) {
        when (entityType) {
            EntityType.DOCUMENTARY -> getDocumentariesUseCase.filter(entityCategory, country.iso)
                .map { it.toDocumentaryUi() }
                .doOnError { _filterEvent.value = FilterEvent.Failure }
                .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }

            EntityType.BOOK -> getBooksUseCase.filter(entityCategory, country.iso)
                .map { it.toBookUi() }
                .doOnError { _filterEvent.value = FilterEvent.Failure }
                .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }

            EntityType.MOVIE -> getMoviesUseCase.filter(entityCategory, country.iso)
                .map { it.toMovieUi() }
                .doOnError { _filterEvent.value = FilterEvent.Failure }
                .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }

            EntityType.SHORT -> getSortsUseCase.filter(entityCategory, country.iso)
                .map { it.toShortUi() }
                .doOnError { _filterEvent.value = FilterEvent.Failure }
                .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }

            EntityType.GAME -> throw IllegalStateException("there shoudn't be filter by country in games")
        }
    }

    override fun onCleared() = disposeSubscriptions()

    sealed interface FetchAllEvent {
        data class Success(val entries: List<EntryUi>) : FetchAllEvent
        data object Failure : FetchAllEvent
        data object Empty : FetchAllEvent
    }

    sealed interface FilterEvent {
        data class Success(val entries: List<EntryUi>) : FilterEvent
        data object Failure : FilterEvent
    }

    sealed interface FindEntityEvent {
        data class Success(val entry: Any) : FindEntityEvent
        data object Failure : FindEntityEvent
    }
}
