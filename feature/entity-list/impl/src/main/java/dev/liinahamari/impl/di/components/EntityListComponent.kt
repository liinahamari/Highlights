package dev.liinahamari.impl.di.components

import dagger.Component
import dev.liinahamari.api.EntityListApi
import dev.liinahamari.api.EntityListDependencies
import dev.liinahamari.impl.di.modules.AppModule
import dev.liinahamari.impl.di.modules.CachedCountriesDbModule
import dev.liinahamari.impl.di.modules.EntriesDatabaseModule
import dev.liinahamari.impl.di.modules.RepoModule
import dev.liinahamari.impl.di.modules.UseCasesModule
import javax.inject.Singleton

@Component(
    dependencies = [EntityListDependencies::class],
    modules = [EntriesDatabaseModule::class, CachedCountriesDbModule::class, UseCasesModule::class, RepoModule::class, AppModule::class]
)
@Singleton
internal interface EntityListComponent : EntityListApi {
    @Component.Factory
    interface Factory {
        fun create(dependencies: EntityListDependencies): EntityListComponent
    }
}

fun EntityListApi.Companion.create(dependencies: EntityListDependencies): EntityListApi =
    DaggerEntityListComponent.factory().create(dependencies)
