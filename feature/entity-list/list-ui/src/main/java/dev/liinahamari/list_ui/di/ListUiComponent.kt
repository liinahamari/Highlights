package dev.liinahamari.list_ui.di

import dagger.Component
import dev.liinahamari.api.EntityListApi
import dev.liinahamari.list_ui.entries_list.EntriesFragment
import dev.liinahamari.list_ui.single_entity.EntryFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddBookDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddDocumentaryDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddMovieDialogFragment
import javax.inject.Singleton

@Component(
    dependencies = [EntityListApi::class],
    modules = [ViewModelBuilderModule::class]
)
@Singleton
internal interface ListUiComponent {
    @Component.Factory
    interface Factory {
        fun create(dependencies: EntityListApi): ListUiComponent
    }

    fun inject(fragment: AddDocumentaryDialogFragment)
    fun inject(fragment: EntriesFragment)
    fun inject(fragment: EntryFragment)
    fun inject(fragment: AddBookDialogFragment)
    fun inject(fragment: AddMovieDialogFragment)
}
