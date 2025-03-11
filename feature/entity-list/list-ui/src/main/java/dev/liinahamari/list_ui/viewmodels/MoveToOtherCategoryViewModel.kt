package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.api.domain.usecases.get.GetGamesUseCase
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.api.domain.usecases.get.GetShortsUseCase
import dev.liinahamari.api.domain.usecases.save.SaveBookUseCase
import dev.liinahamari.api.domain.usecases.save.SaveDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.save.SaveGameUseCase
import dev.liinahamari.api.domain.usecases.save.SaveMovieUseCase
import dev.liinahamari.api.domain.usecases.save.SaveShortsUseCase
import dev.liinahamari.core.RxSubscriptionDelegateImpl
import dev.liinahamari.core.RxSubscriptionsDelegate
import dev.liinahamari.core.SingleLiveEvent
import javax.inject.Inject

class ChangeGameCategoryViewModel (
    private val getGamesUseCase: GetGamesUseCase,
    private val saveGameUseCase: SaveGameUseCase
) : MoveToOtherCategoryViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun moveToOtherCategory(
        actualCategory: Category,
        desirableCategory: Category,
        selection: Set<Long>
    ) {
        getGamesUseCase.findByIds(actualCategory, selection)
            .map { it.map { it.copy(category = desirableCategory) }.toTypedArray() }
            .flatMapCompletable(saveGameUseCase::saveGames)
            .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
            .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success }
    }
}

class ChangeDocumentaryCategoryViewModel (
    private val getDocumentariesUseCase: GetDocumentariesUseCase,
    private val saveDocumentaryUseCase: SaveDocumentaryUseCase
) : MoveToOtherCategoryViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun moveToOtherCategory(
        actualCategory: Category,
        desirableCategory: Category,
        selection: Set<Long>
    ) {
        getDocumentariesUseCase.findByIds(actualCategory, selection)
            .map { it.map { it.copy(category = desirableCategory) }.toTypedArray() }
            .flatMapCompletable(saveDocumentaryUseCase::saveDocumentaries)
            .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
            .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success }
    }
}

class ChangeShortCategoryViewModel (
    private val getShortsUseCase: GetShortsUseCase,
    private val saveShortsUseCase: SaveShortsUseCase
) : MoveToOtherCategoryViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun moveToOtherCategory(
        actualCategory: Category,
        desirableCategory: Category,
        selection: Set<Long>
    ) {
        getShortsUseCase.findByIds(actualCategory, selection)
            .map { it.map { it.copy(category = desirableCategory) }.toTypedArray() }
            .flatMapCompletable(saveShortsUseCase::saveShorts)
            .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
            .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success }
    }
}

class ChangeMovieCategoryViewModel (
    private val getMoviesUseCase: GetMoviesUseCase,
    private val saveMovieUseCase: SaveMovieUseCase
) : MoveToOtherCategoryViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun moveToOtherCategory(
        actualCategory: Category,
        desirableCategory: Category,
        selection: Set<Long>
    ) {
        getMoviesUseCase.findByIds(actualCategory, selection)
            .map { it.map { it.copy(category = desirableCategory) }.toTypedArray() }
            .flatMapCompletable(saveMovieUseCase::saveMovies)
            .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
            .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success }
    }
}

class ChangeBookCategoryViewModel (
    private val getBooksUseCase: GetBooksUseCase,
    private val saveBookUseCase: SaveBookUseCase
) : MoveToOtherCategoryViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    override fun moveToOtherCategory(
        actualCategory: Category,
        desirableCategory: Category,
        selection: Set<Long>
    ) {
        getBooksUseCase.findByIds(actualCategory, selection)
            .map { it.map { it.copy(category = desirableCategory) }.toTypedArray() }
            .flatMapCompletable(saveBookUseCase::saveBooks)
            .doOnError { _saveEntityEvent.value = SaveEntityEvent.Failure }
            .subscribeUi { _saveEntityEvent.value = SaveEntityEvent.Success }
    }
}

open class MoveToOtherCategoryViewModel @Inject constructor(): ViewModel(), RxSubscriptionsDelegate by RxSubscriptionDelegateImpl() {
    protected val _saveEntityEvent = SingleLiveEvent<SaveEntityEvent>()
    val saveEntityEvent: LiveData<SaveEntityEvent> get() = _saveEntityEvent

    open fun moveToOtherCategory(actualCategory: Category, desirableCategory: Category, selection: Set<Long>) {}

    sealed class SaveEntityEvent {
        data object Success : SaveEntityEvent()
        data object Failure : SaveEntityEvent()
    }
}
