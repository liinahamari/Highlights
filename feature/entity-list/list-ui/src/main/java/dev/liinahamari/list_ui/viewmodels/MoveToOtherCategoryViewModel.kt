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
        id: Long,
        adapterPosition: Int
    ) {
        when (entityType) {
            EntityType.DOCUMENTARY -> getDocumentariesUseCase.findById(actualCategory, id)
                .map { it.copy(category = desirableCategory) }
                .flatMapCompletable(saveDocumentaryUseCase::saveDocumentary)
                .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
                .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success(adapterPosition) }

            EntityType.BOOK -> getBooksUseCase.findById(actualCategory, id)
                .map { it.copy(category = desirableCategory) }
                .flatMapCompletable(saveBookUseCase::saveBook)
                .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
                .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success(adapterPosition) }

            EntityType.MOVIE -> getMoviesUseCase.findById(actualCategory, id)
                .map { it.copy(category = desirableCategory) }
                .flatMapCompletable(saveMovieUseCase::saveMovie)
                .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
                .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success(adapterPosition) }

            EntityType.GAME -> getGamesUseCase.findById(actualCategory, id)
                .map { it.copy(category = desirableCategory) }
                .flatMapCompletable(saveGameUseCase::saveGame)
                .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
                .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success(adapterPosition) }
        }
    }

    sealed class SaveEntityEvent {
        data class Success(val adapterPosition: Int) : SaveEntityEvent()
        object Failure : SaveEntityEvent()
    }
}
