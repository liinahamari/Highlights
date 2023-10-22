package dev.liinahamari.entity_list

import dev.liinahamari.api.EntityListApi
import dev.liinahamari.api.EntityListDependencies

import dev.liinahamari.impl.di.components.create

object EntityListFactory {
    @JvmStatic fun getApi(dependencies: EntityListDependencies) = EntityListApi.create(dependencies)
}
