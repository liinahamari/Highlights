package dev.liinahamari.list_ui.single_entity

import dev.liinahamari.api.domain.entities.Category

data class Entity(
    val id: Long,
    val name: String,
    val countries: String,
    val genre: String,
    val category: Category,
    val type: EntityType
)
