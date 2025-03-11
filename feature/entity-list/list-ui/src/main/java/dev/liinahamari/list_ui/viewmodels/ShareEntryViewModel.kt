package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.ShareMessage
import dev.liinahamari.api.domain.entities.toBookUi
import dev.liinahamari.api.domain.entities.toDocumentaryUi
import dev.liinahamari.api.domain.entities.toGameUi
import dev.liinahamari.api.domain.entities.toMovieUi
import dev.liinahamari.api.domain.entities.toShortUi
import dev.liinahamari.api.domain.usecases.ComposeShareMessageUseCase
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.api.domain.usecases.get.GetShortsUseCase
import dev.liinahamari.core.RxSubscriptionDelegateImpl
import dev.liinahamari.core.RxSubscriptionsDelegate
import dev.liinahamari.core.SingleLiveEvent
import javax.inject.Inject

class ShareGameViewModel(
    private val getGamesUseCase: GetGamesUseCase,
    private val composeShareMessageUseCase: ComposeShareMessageUseCase,
) : ShareEntryViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun getById(category: Category, selection: Set<Long>) {
        getGamesUseCase.findByIds(category, selection).map { it.toGameUi() }
            .map(composeShareMessageUseCase::composeGameShareMessage)
            .doOnError { _shareEntryEvent.value = ShareEntry.Failure }
            .subscribeUi { _shareEntryEvent.value = ShareEntry.Success(it) }
    }
}

class ShareBookViewModel(
    private val getBooksUseCase: GetBooksUseCase,
    private val composeShareMessageUseCase: ComposeShareMessageUseCase,
) : ShareEntryViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun getById(category: Category, selection: Set<Long>) {
        getBooksUseCase.findByIds(category, selection).map { it.toBookUi() }
            .map(composeShareMessageUseCase::composeBooksShareMessage)
            .doOnError { _shareEntryEvent.value = ShareEntry.Failure }
            .subscribeUi { _shareEntryEvent.value = ShareEntry.Success(it) }
    }
}

class ShareDocumentaryViewModel(
    private val getDocumentariesUseCase: GetDocumentariesUseCase,
    private val composeShareMessageUseCase: ComposeShareMessageUseCase,
) : ShareEntryViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun getById(category: Category, selection: Set<Long>) {
        getDocumentariesUseCase.findByIds(category, selection)
            .map { it.toDocumentaryUi() }
            .map(composeShareMessageUseCase::composeDocumentariesShareMessage)
            .doOnError { _shareEntryEvent.value = ShareEntry.Failure }
            .subscribeUi { _shareEntryEvent.value = ShareEntry.Success(it) }
    }
}

class ShareMovieViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val composeShareMessageUseCase: ComposeShareMessageUseCase,
) : ShareEntryViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun getById(category: Category, selection: Set<Long>) {
        getMoviesUseCase.findByIds(category, selection).map { it.toMovieUi() }
            .map(composeShareMessageUseCase::composeMovieShareMessage)
            .doOnError { _shareEntryEvent.value = ShareEntry.Failure }
            .subscribeUi { _shareEntryEvent.value = ShareEntry.Success(it) }
    }
}

class ShareShortViewModel(
    private val getShortsUseCase: GetShortsUseCase,
    private val composeShareMessageUseCase: ComposeShareMessageUseCase,
) : ShareEntryViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun getById(category: Category, selection: Set<Long>) {
        getShortsUseCase.findByIds(category, selection).map { it.toShortUi() }
            .map(composeShareMessageUseCase::composeShortsShareMessage)
            .doOnError { _shareEntryEvent.value = ShareEntry.Failure }
            .subscribeUi { _shareEntryEvent.value = ShareEntry.Success(it) }
    }
}

open class ShareEntryViewModel @Inject constructor() : ViewModel(),
    RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    protected val _shareEntryEvent = SingleLiveEvent<ShareEntry>()
    val shareEntryEvent: LiveData<ShareEntry> get() = _shareEntryEvent
    open fun getById(category: Category, selection: Set<Long>) {}

    sealed class ShareEntry {
        data class Success(val data: ShareMessage) : ShareEntry()
        data object Failure : ShareEntry()
    }
}
