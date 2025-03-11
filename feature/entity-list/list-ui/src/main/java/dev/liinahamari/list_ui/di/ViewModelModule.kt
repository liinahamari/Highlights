package dev.liinahamari.list_ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import dev.liinahamari.api.domain.usecases.ComposeShareMessageUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteBookUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteDocumentaryUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteGameUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteMovieUseCase
import dev.liinahamari.api.domain.usecases.delete.DeleteShortUseCase
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
import dev.liinahamari.list_ui.entries_list.EntriesViewModel
import dev.liinahamari.list_ui.viewmodels.CachedCountriesViewModel
import dev.liinahamari.list_ui.viewmodels.ChangeBookCategoryViewModel
import dev.liinahamari.list_ui.viewmodels.ChangeDocumentaryCategoryViewModel
import dev.liinahamari.list_ui.viewmodels.ChangeGameCategoryViewModel
import dev.liinahamari.list_ui.viewmodels.ChangeMovieCategoryViewModel
import dev.liinahamari.list_ui.viewmodels.ChangeShortCategoryViewModel
import dev.liinahamari.list_ui.viewmodels.DeleteBookViewModel
import dev.liinahamari.list_ui.viewmodels.DeleteDocumentaryViewModel
import dev.liinahamari.list_ui.viewmodels.DeleteEntryViewModel
import dev.liinahamari.list_ui.viewmodels.DeleteGameViewModel
import dev.liinahamari.list_ui.viewmodels.DeleteMovieViewModel
import dev.liinahamari.list_ui.viewmodels.DeleteShortViewModel
import dev.liinahamari.list_ui.viewmodels.FetchBooksViewModel
import dev.liinahamari.list_ui.viewmodels.FetchDocumentariesViewModel
import dev.liinahamari.list_ui.viewmodels.FetchViewModel
import dev.liinahamari.list_ui.viewmodels.FetchGamesViewModel
import dev.liinahamari.list_ui.viewmodels.FetchMoviesViewModel
import dev.liinahamari.list_ui.viewmodels.FetchShortsViewModel
import dev.liinahamari.list_ui.viewmodels.MainActivityViewModel
import dev.liinahamari.list_ui.viewmodels.MoveToOtherCategoryViewModel
import dev.liinahamari.list_ui.viewmodels.SaveEntryViewModel
import dev.liinahamari.list_ui.viewmodels.ShareBookViewModel
import dev.liinahamari.list_ui.viewmodels.ShareDocumentaryViewModel
import dev.liinahamari.list_ui.viewmodels.ShareEntryViewModel
import dev.liinahamari.list_ui.viewmodels.ShareGameViewModel
import dev.liinahamari.list_ui.viewmodels.ShareMovieViewModel
import dev.liinahamari.list_ui.viewmodels.ShareShortViewModel
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
abstract class ViewModelBuilderModule {
    enum class ENTITY_TYPE { BOOK, MOVIE, SHORT, DOCUMENTARY, GAME }

    @Binds
    @IntoMap
    @ViewModelKey(FetchViewModel::class)
    abstract fun fetchEntriesViewModel(viewModel: FetchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShareEntryViewModel::class)
    abstract fun shareEntryViewModel(viewModel: ShareEntryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SaveEntryViewModel::class)
    abstract fun saveEntryViewModel(viewModel: SaveEntryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EntriesViewModel::class)
    abstract fun entriesViewModel(viewModel: EntriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeleteEntryViewModel::class)
    abstract fun deleteEntryViewModel(viewModel: DeleteEntryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoveToOtherCategoryViewModel::class)
    abstract fun moveToOtherCategoryViewModelViewModel(viewModel: MoveToOtherCategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun mainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CachedCountriesViewModel::class)
    abstract fun cachedCountriesViewModel(viewModel: CachedCountriesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}


@Singleton
class ViewModelFactory @Inject constructor(
    private val composeShareMessageUseCase: ComposeShareMessageUseCase,
    private val getGamesUseCase: GetGamesUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getShortsUseCase: GetShortsUseCase,
    private val getBooksUseCase: GetBooksUseCase,
    private val getDocumentariesUseCase: GetDocumentariesUseCase,
    private val deleteBookUseCase: DeleteBookUseCase,
    private val deleteGameUseCase: DeleteGameUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val deleteShortUseCase: DeleteShortUseCase,
    private val deleteDocumentaryUseCase: DeleteDocumentaryUseCase,
    private val saveBookUseCase: SaveBookUseCase,
    private val saveGameUseCase: SaveGameUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
    private val saveShortsUseCase: SaveShortsUseCase,
    private val saveDocumentaryUseCase: SaveDocumentaryUseCase,
    @Named(ENTITY) private val entityType: ViewModelBuilderModule.ENTITY_TYPE,
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            FetchViewModel::class.java ->
                when (entityType) {
                    ViewModelBuilderModule.ENTITY_TYPE.GAME -> FetchGamesViewModel(getGamesUseCase)
                    ViewModelBuilderModule.ENTITY_TYPE.MOVIE -> FetchMoviesViewModel(getMoviesUseCase)
                    ViewModelBuilderModule.ENTITY_TYPE.SHORT -> FetchShortsViewModel(getShortsUseCase)
                    ViewModelBuilderModule.ENTITY_TYPE.BOOK -> FetchBooksViewModel(getBooksUseCase)
                    ViewModelBuilderModule.ENTITY_TYPE.DOCUMENTARY -> FetchDocumentariesViewModel(
                        getDocumentariesUseCase
                    )
                } as T

            ShareEntryViewModel::class.java -> when (entityType) {
                ViewModelBuilderModule.ENTITY_TYPE.GAME -> ShareGameViewModel(
                    getGamesUseCase,
                    composeShareMessageUseCase
                )

                ViewModelBuilderModule.ENTITY_TYPE.MOVIE -> ShareMovieViewModel(
                    getMoviesUseCase,
                    composeShareMessageUseCase
                )

                ViewModelBuilderModule.ENTITY_TYPE.SHORT -> ShareShortViewModel(
                    getShortsUseCase,
                    composeShareMessageUseCase
                )

                ViewModelBuilderModule.ENTITY_TYPE.BOOK -> ShareBookViewModel(
                    getBooksUseCase,
                    composeShareMessageUseCase
                )

                ViewModelBuilderModule.ENTITY_TYPE.DOCUMENTARY -> ShareDocumentaryViewModel(
                    getDocumentariesUseCase, composeShareMessageUseCase
                )
            } as T

            DeleteEntryViewModel::class.java -> when (entityType) {
                ViewModelBuilderModule.ENTITY_TYPE.GAME -> DeleteGameViewModel(deleteGameUseCase)
                ViewModelBuilderModule.ENTITY_TYPE.MOVIE -> DeleteMovieViewModel(deleteMovieUseCase)
                ViewModelBuilderModule.ENTITY_TYPE.SHORT -> DeleteShortViewModel(deleteShortUseCase)
                ViewModelBuilderModule.ENTITY_TYPE.BOOK -> DeleteBookViewModel(deleteBookUseCase)
                ViewModelBuilderModule.ENTITY_TYPE.DOCUMENTARY -> DeleteDocumentaryViewModel(deleteDocumentaryUseCase)
            } as T

            MoveToOtherCategoryViewModel::class.java -> when (entityType) {
                ViewModelBuilderModule.ENTITY_TYPE.GAME -> ChangeGameCategoryViewModel(getGamesUseCase, saveGameUseCase)
                ViewModelBuilderModule.ENTITY_TYPE.MOVIE -> ChangeMovieCategoryViewModel(
                    getMoviesUseCase,
                    saveMovieUseCase
                )

                ViewModelBuilderModule.ENTITY_TYPE.SHORT -> ChangeShortCategoryViewModel(
                    getShortsUseCase,
                    saveShortsUseCase
                )

                ViewModelBuilderModule.ENTITY_TYPE.BOOK -> ChangeBookCategoryViewModel(getBooksUseCase, saveBookUseCase)
                ViewModelBuilderModule.ENTITY_TYPE.DOCUMENTARY -> ChangeDocumentaryCategoryViewModel(
                    getDocumentariesUseCase,
                    saveDocumentaryUseCase
                )
            } as T

            else -> {
                val creator = creators[modelClass] ?: creators.entries.firstOrNull {
                    modelClass.isAssignableFrom(it.key)
                }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
                try {
                    @Suppress("UNCHECKED_CAST") return creator.get() as T
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
