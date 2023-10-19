package dev.liinahamari.api.domain.entities

data class Documentary(
    val id: Long,
    val category: Category,
    val year: Int,
    val posterUrl: String,
    val countryCodes: List<String>,
    val name: String
)
