package dev.liinahamari.summary_ui.di

import dagger.BindsInstance
import dagger.Component
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.summary_ui.SummaryFragment
import dev.liinahamari.summary_ui.di.modules.ViewModelBuilderModule
import javax.inject.Singleton

@Component(modules = [ViewModelBuilderModule::class])
@Singleton interface SummaryComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance fun databaseCountersUseCase(databaseCountersUseCase: DatabaseCountersUseCase): Builder
        fun build(): SummaryComponent
    }

    fun inject(fragment: SummaryFragment)
}
