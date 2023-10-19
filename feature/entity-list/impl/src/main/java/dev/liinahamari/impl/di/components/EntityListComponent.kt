package dev.liinahamari.impl.di.components

import dagger.Component
import dev.liinahamari.api.EntityListApi
import dev.liinahamari.api.EntityListDependencies
import dev.liinahamari.impl.di.modules.DatabaseModule
import dev.liinahamari.impl.di.modules.RepoModule
import dev.liinahamari.impl.di.modules.UseCasesModule
import javax.inject.Singleton

@Component(
    dependencies = [EntityListDependencies::class],
    modules = [DatabaseModule::class, UseCasesModule::class, RepoModule::class]
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
