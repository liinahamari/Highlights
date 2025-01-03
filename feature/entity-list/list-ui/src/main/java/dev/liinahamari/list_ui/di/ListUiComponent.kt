package dev.liinahamari.list_ui.di

import dagger.Component
import dev.liinahamari.api.EntityListApi
import dev.liinahamari.api.SettingsApi
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.entries_list.EntriesFragment
import dev.liinahamari.list_ui.single_entity.EntryFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddFragment
import javax.inject.Singleton

@Component(
    dependencies = [EntityListApi::class, SettingsApi::class],
    modules = [ViewModelBuilderModule::class]
)
@Singleton
internal interface ListUiComponent {
    @Component.Factory
    interface Factory {
        fun create(deps1: EntityListApi, deps2: SettingsApi): ListUiComponent
    }

    fun inject(fragment: EntriesFragment)
    fun inject(activity: MainActivity)
    fun inject(fragment: EntryFragment)
    fun inject(fragment: AddFragment)
}
