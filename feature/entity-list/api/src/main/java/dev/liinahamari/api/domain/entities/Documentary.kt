package dev.liinahamari.api.domain.entities

data class Documentary(
    val id: Long = 0L,
    val category: Category,
    val year: Int,
    val posterUrl: String?,
    val countryCodes: List<String>,
    val name: String,
    val description: String
) {
    companion object {
        fun default(category: Category = Category.GOOD) = Documentary(
            0L, category, 0, "", listOf(), "", ""
        )
    }
}

