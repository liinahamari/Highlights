package dev.liinahamari.settings_ui.di

import dagger.Component
import dev.liinahamari.api.SettingsApi
import dev.liinahamari.settings_ui.SettingsFragment
import javax.inject.Singleton

@Component(
    dependencies = [SettingsApi::class],
    modules = [ViewModelBuilderModule::class]
)
@Singleton
internal interface SettingsUiComponent {
    @Component.Factory
    interface Factory {
        fun create(dependencies: SettingsApi): SettingsUiComponent
    }

    fun inject(fragment: SettingsFragment)
}
