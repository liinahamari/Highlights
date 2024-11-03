package dev.liinahamari.impl.di.components

import dagger.Component
import dev.liinahamari.api.SettingsApi
import dev.liinahamari.api.SettingsDeps
import dev.liinahamari.impl.di.modules.SettingsModule
import javax.inject.Singleton

@Component(
    dependencies = [SettingsDeps::class],
    modules = [SettingsModule::class]
)
@Singleton
internal interface SettingsComponent : SettingsApi {
    @Component.Factory
    interface Factory {
        fun create(dependencies: SettingsDeps): SettingsComponent
    }
}

fun SettingsApi.Companion.create(dependencies: SettingsDeps): SettingsApi =
    DaggerSettingsComponent.factory().create(dependencies)
