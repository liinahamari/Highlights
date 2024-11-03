package dev.liinahamari.impl.di.modules

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dev.liinahamari.api.LeakCanaryController
import dev.liinahamari.api.PreferencesRepo
import dev.liinahamari.impl.LeakCanaryControllerImpl
import dev.liinahamari.impl.data.repos.PreferencesRepoImpl
import javax.inject.Named
import javax.inject.Singleton

internal const val APP_CONTEXT = "app_ctx"

@Module
class SettingsModule {
    @Provides
    @Singleton
    @Named(APP_CONTEXT)
    fun getContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun getLcController(): LeakCanaryController = LeakCanaryControllerImpl()

    @Provides
    @Singleton
    fun prefRepo(@Named(APP_CONTEXT) context: Context): PreferencesRepo =
        PreferencesRepoImpl(PreferenceManager.getDefaultSharedPreferences(context), context)
}
