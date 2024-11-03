package dev.liinahamari.settings

import dev.liinahamari.api.SettingsApi
import dev.liinahamari.api.SettingsDeps

import dev.liinahamari.impl.di.components.create

//consider a scope
object SettingsFactory {
    @JvmStatic fun getApi(dependencies: SettingsDeps) = SettingsApi.create(dependencies)
}
