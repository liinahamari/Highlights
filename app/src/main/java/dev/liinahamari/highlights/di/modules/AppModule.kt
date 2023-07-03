package dev.liinahamari.highlights.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

internal const val APP_CONTEXT = "application_context"

@Module
class AppModule {
    @Provides
    @Singleton
    @Named(APP_CONTEXT)
    fun bindContext(app: Application): Context = app.applicationContext
}
