package dev.liinahamari.list_ui.di

import dagger.BindsInstance
import dagger.Component
import dev.liinahamari.api.EntityListApi
import dev.liinahamari.api.SettingsApi
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.entries_list.EntriesFragment
import dev.liinahamari.list_ui.single_entity.EntryFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddFragment
import javax.inject.Named
import javax.inject.Singleton

@Component(
    dependencies = [EntityListApi::class, SettingsApi::class],
    modules = [ViewModelBuilderModule::class]
)
@Singleton
internal interface ListUiComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun entityType(@Named(ENTITY) apiType: ViewModelBuilderModule.ENTITY_TYPE): Builder
        fun deps(deps1: EntityListApi): Builder
        fun deps2(deps2: SettingsApi): Builder
        fun create(): ListUiComponent
    }

    fun inject(fragment: EntriesFragment)
    fun inject(activity: MainActivity)
    fun inject(fragment: AddFragment)
}

const val ENTITY = "entity_type"
