package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.ShareMessage
import dev.liinahamari.api.domain.entities.toBookUi
import dev.liinahamari.api.domain.entities.toDocumentaryUi
import dev.liinahamari.api.domain.entities.toGameUi
import dev.liinahamari.api.domain.entities.toMovieUi
import dev.liinahamari.api.domain.entities.toUi
import dev.liinahamari.api.domain.usecases.ComposeShareMessageUseCase
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.core.RxSubscriptionDelegateImpl
import dev.liinahamari.core.RxSubscriptionsDelegate
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.list_ui.single_entity.EntityType
import javax.inject.Inject

class ShareEntryViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase,
    private val getDocumentariesUseCase: GetDocumentariesUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getBooksUseCase: GetBooksUseCase,
    private val composeShareMessageUseCase: ComposeShareMessageUseCase,
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _shareEntryEvent = SingleLiveEvent<ShareEntry>()
    val shareEntryEvent: LiveData<ShareEntry> get() = _shareEntryEvent

    fun getEntitiesById(
        category: Category,
        entityType: EntityType,
        selection: Set<Long>
    ) {
        when (entityType) {
            EntityType.DOCUMENTARY -> getDocumentariesUseCase.findByIds(category, selection).map { it.toDocumentaryUi() }
            EntityType.BOOK -> getBooksUseCase.findByIds(category, selection).map { it.toBookUi() }
            EntityType.MOVIE -> getMoviesUseCase.findByIds(category, selection).map { it.toMovieUi() }
            EntityType.GAME -> getGamesUseCase.findByIds(category, selection).map { it.toGameUi() }
        }
            .map(composeShareMessageUseCase::composeShareMessage)
            .doOnError { _shareEntryEvent.value = ShareEntry.Failure }
            .subscribeUi { _shareEntryEvent.value = ShareEntry.Success(it) }

    }

    sealed class ShareEntry {
        data class Success(val data: ShareMessage) : ShareEntry()
        object Failure : ShareEntry()
    }
}
