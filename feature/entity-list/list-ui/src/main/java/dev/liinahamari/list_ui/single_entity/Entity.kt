package dev.liinahamari.list_ui.single_entity

data class Entity(
    val id: Long,
    val name: String,
    val countries: String,
    val genre: String,
    val category: EntityCategory,
    val type: EntityType
)
