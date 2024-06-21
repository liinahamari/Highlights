package dev.liinahamari.api.domain.entities

data class EntryUi(
    val id: Long,
    val title: String,
    val countries: List<String>,
    val genres: String,
    val year: Int,
    val description: String,
    val posterUrl: String?,
    val clazz: Class<*>,
    val tmdbUrl: String?
)
