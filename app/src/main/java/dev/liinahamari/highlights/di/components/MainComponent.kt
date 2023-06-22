package dev.liinahamari.highlights.di.components

import dagger.BindsInstance
import dagger.Component
import dev.liinahamari.highlights.App
import dev.liinahamari.highlights.di.modules.AppModule
import dev.liinahamari.highlights.di.modules.DatabaseModule
import dev.liinahamari.highlights.di.modules.ViewModelBuilderModule
import dev.liinahamari.highlights.ui.single_entity.EntryFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, ViewModelBuilderModule::class])
interface MainComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: App): Builder
        fun build(): MainComponent
    }

    fun inject(fragment: EntryFragment)
}
