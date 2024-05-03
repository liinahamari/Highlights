package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.api.domain.usecases.save.SaveBookUseCase
import dev.liinahamari.api.domain.usecases.save.SaveDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.save.SaveGameUseCase
import dev.liinahamari.api.domain.usecases.save.SaveMovieUseCase
import dev.liinahamari.core.RxSubscriptionDelegateImpl
import dev.liinahamari.core.RxSubscriptionsDelegate
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.list_ui.single_entity.EntityType
import javax.inject.Inject

class MoveToOtherCategoryViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase,
    private val getDocumentariesUseCase: GetDocumentariesUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getBooksUseCase: GetBooksUseCase,
    private val saveGameUseCase: SaveGameUseCase,
    private val saveDocumentaryUseCase: SaveDocumentaryUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
    private val saveBookUseCase: SaveBookUseCase
) : ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    private val _saveEntityEvent = SingleLiveEvent<SaveEntityEvent>()
    val saveEntityEvent: LiveData<SaveEntityEvent> get() = _saveEntityEvent

    fun moveToOtherCategory(
        actualCategory: Category,
        desirableCategory: Category,
        entityType: EntityType,
        selection: Set<Long>
    ) {
        when (entityType) {
            EntityType.DOCUMENTARY -> getDocumentariesUseCase.findByIds(actualCategory, selection)
                .map { it.map { it.copy(category = desirableCategory) }.toTypedArray() }
                .flatMapCompletable(saveDocumentaryUseCase::saveDocumentaries)
                .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
                .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success }

            EntityType.BOOK -> getBooksUseCase.findByIds(actualCategory, selection)
                .map { it.map { it.copy(category = desirableCategory) }.toTypedArray() }
                .flatMapCompletable(saveBookUseCase::saveBooks)
                .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
                .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success }

            EntityType.MOVIE -> getMoviesUseCase.findByIds(actualCategory, selection)
                .map { it.map { it.copy(category = desirableCategory) }.toTypedArray() }
                .flatMapCompletable(saveMovieUseCase::saveMovies)
                .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
                .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success }

            EntityType.GAME -> getGamesUseCase.findByIds(actualCategory, selection)
                .map { it.map { it.copy(category = desirableCategory) }.toTypedArray() }
                .flatMapCompletable(saveGameUseCase::saveGames)
                .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
                .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success }
        }
    }

    sealed class SaveEntityEvent {
        object Success : SaveEntityEvent()
        object Failure : SaveEntityEvent()
    }
}
