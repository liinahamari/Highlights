package dev.liinahamari.api

import android.app.Application

interface SettingsApi {
    val leakCanaryController: LeakCanaryController
    val preferencesRepo: PreferencesRepo
    val app: Application

    companion object
}
