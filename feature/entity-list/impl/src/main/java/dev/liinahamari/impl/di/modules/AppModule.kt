package dev.liinahamari.impl.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

internal const val APP_CONTEXT = "app_ctx"

@Module
class AppModule {
    @Provides
    @Singleton
    @Named(APP_CONTEXT)
    fun bindContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun preferences(@Named(APP_CONTEXT) ctx: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)
}
