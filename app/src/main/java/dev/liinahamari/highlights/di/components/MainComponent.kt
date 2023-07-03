package dev.liinahamari.highlights.di.components

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dev.liinahamari.highlights.di.modules.AppModule
import dev.liinahamari.highlights.di.modules.BackupModule
import dev.liinahamari.highlights.di.modules.DatabaseModule
import dev.liinahamari.highlights.di.modules.ViewModelBuilderModule
import dev.liinahamari.highlights.ui.entries_list.EntriesFragment
import dev.liinahamari.highlights.ui.single_entity.EntryFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, ViewModelBuilderModule::class, BackupModule::class])
interface MainComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): MainComponent
    }

    fun inject(fragment: EntryFragment)
    fun inject(fragment: EntriesFragment)
}
