package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Country
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
import javax.inject.Inject

class FetchGamesViewModel(
    private val getGamesUseCase: GetGamesUseCase
) : FetchViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun fetchEntries(entityCategory: Category) {
        getGamesUseCase.getAllGames(entityCategory)
            .subscribeUi { result ->
                when (result) {
                    is GetAllGamesResult.Success -> _fetchAllEvent.value = FetchAllEvent.Success(result.data)
                    is GetAllGamesResult.EmptyList -> _fetchAllEvent.value = FetchAllEvent.Empty
                    is GetAllGamesResult.Error -> _fetchAllEvent.value = FetchAllEvent.Failure
                }
            }
    }

    override fun filter(category: Category, country: Country) {
        throw IllegalStateException("there shoudn't be filter by country in games")
    }
}

class FetchBooksViewModel(
    private val getBooksUseCase: GetBooksUseCase
) : FetchViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun fetchEntries(entityCategory: Category) {
        getBooksUseCase.getAllBooks(entityCategory)
            .subscribeUi { result ->
                when (result) {
                    is GetAllBooksResult.Success -> _fetchAllEvent.value = FetchAllEvent.Success(result.data)
                    is GetAllBooksResult.EmptyList -> _fetchAllEvent.value = FetchAllEvent.Empty
                    is GetAllBooksResult.Error -> _fetchAllEvent.value = FetchAllEvent.Failure
                }
            }
    }

    override fun filter(category: Category, country: Country) {
        getBooksUseCase.filter(category, country.iso)
            .map { it.toBookUi() }
            .doOnError { _filterEvent.value = FilterEvent.Failure }
            .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }
    }
}

class FetchDocumentariesViewModel(
    private val getDocumentariesUseCase: GetDocumentariesUseCase
) : FetchViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun fetchEntries(entityCategory: Category) {
        getDocumentariesUseCase.getAllDocumentaries(entityCategory)
            .subscribeUi { result ->
                when (result) {
                    is GetAllDocumentariesResult.Success -> _fetchAllEvent.value = FetchAllEvent.Success(result.data)
                    is GetAllDocumentariesResult.EmptyList -> _fetchAllEvent.value = FetchAllEvent.Empty
                    is GetAllDocumentariesResult.Error -> _fetchAllEvent.value = FetchAllEvent.Failure
                }
            }
    }

    override fun filter(category: Category, country: Country) {
        getDocumentariesUseCase.filter(category, country.iso)
            .map { it.toDocumentaryUi() }
            .doOnError { _filterEvent.value = FilterEvent.Failure }
            .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }
    }
}

class FetchShortsViewModel(
    private val getShortsUseCase: GetShortsUseCase
) : FetchViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun fetchEntries(entityCategory: Category) {
        getShortsUseCase.getAllShorts(entityCategory)
            .subscribeUi { result ->
                when (result) {
                    is GetAllShortsResult.Success -> _fetchAllEvent.value = FetchAllEvent.Success(result.data)
                    is GetAllShortsResult.EmptyList -> _fetchAllEvent.value = FetchAllEvent.Empty
                    is GetAllShortsResult.Error -> _fetchAllEvent.value = FetchAllEvent.Failure
                }
            }
    }

    override fun filter(category: Category, country: Country) {
        getShortsUseCase.filter(category, country.iso)
            .map { it.toShortUi() }
            .doOnError { _filterEvent.value = FilterEvent.Failure }
            .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }
    }
}

class FetchMoviesViewModel(
    private val getMoviesUseCase: GetMoviesUseCase
) : FetchViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun fetchEntries(entityCategory: Category) {
        getMoviesUseCase.getAllMovies(entityCategory)
            .subscribeUi { result ->
                when (result) {
                    is GetAllMoviesResult.Success -> _fetchAllEvent.value = FetchAllEvent.Success(result.data)
                    is GetAllMoviesResult.EmptyList -> _fetchAllEvent.value = FetchAllEvent.Empty
                    is GetAllMoviesResult.Error -> _fetchAllEvent.value = FetchAllEvent.Failure
                }
            }
    }

    override fun filter(category: Category, country: Country) {
        getMoviesUseCase.filter(category, country.iso)
            .map { it.toMovieUi() }
            .doOnError { _filterEvent.value = FilterEvent.Failure }
            .subscribeUi { entries -> _filterEvent.value = FilterEvent.Success(entries) }
    }
}

open class FetchViewModel @Inject constructor() : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    protected val _fetchAllEvent = SingleLiveEvent<FetchAllEvent>()
    val fetchAllEvent: LiveData<FetchAllEvent> get() = _fetchAllEvent

    protected val _filterEvent = SingleLiveEvent<FilterEvent>()
    val filterEvent: LiveData<FilterEvent> get() = _filterEvent

    protected val _findEntityEvent = SingleLiveEvent<FindEntityEvent>()
    val findEntityEvent: LiveData<FindEntityEvent> get() = _findEntityEvent

    open fun fetchEntries(entityCategory: Category) {}
    open fun filter(category: Category, country: Country) {}

    override fun onCleared() = disposeSubscriptions()
}

sealed interface FetchAllEvent {
    data class Success<EntryUi>(val entries: List<EntryUi>) : FetchAllEvent
    data object Failure : FetchAllEvent
    data object Empty : FetchAllEvent
}

sealed interface FilterEvent {
    data class Success<EntryUi>(val entries: List<EntryUi>) : FilterEvent
    data object Failure : FilterEvent
}

sealed interface FindEntityEvent {
    data class Success<EntryUi>(val entry: EntryUi) : FindEntityEvent
    data object Failure : FindEntityEvent
}
