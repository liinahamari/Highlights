package dev.liinahamari.impl.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.liinahamari.impl.data.db.CachedCountriesDao
import dev.liinahamari.impl.data.db.CachedCountriesDatabase
import javax.inject.Named
import javax.inject.Singleton

@Module
class CachedCountriesDbModule {
    @Provides
    @Singleton
    fun database(@Named(APP_CONTEXT) context: Context): CachedCountriesDatabase = Room.databaseBuilder(
        context,
        CachedCountriesDatabase::class.java, "cached-countries",
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun cachedCountriesDao(db: CachedCountriesDatabase): CachedCountriesDao = db.cachedCountriesDao()
}
