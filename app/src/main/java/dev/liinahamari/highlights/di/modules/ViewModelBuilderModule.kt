package dev.liinahamari.highlights.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import dev.liinahamari.highlights.ui.entries_list.EntriesViewModel
import dev.liinahamari.highlights.ui.single_entity.DeleteEntryViewModel
import dev.liinahamari.highlights.ui.single_entity.FetchEntriesViewModel
import dev.liinahamari.highlights.ui.single_entity.SaveEntryViewModel
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
abstract class ViewModelBuilderModule {
    @Binds
    @IntoMap
    @ViewModelKey(FetchEntriesViewModel::class)
    abstract fun fetchEntriesViewModel(viewModel: FetchEntriesViewModel): ViewModel

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
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}


@Singleton
class ViewModelFactory @Inject constructor(private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
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

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
