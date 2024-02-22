package dev.liinahamari.api.domain.entities

data class Book(
    val id: Long = 0L,
    val category: Category,
    val year: Int,
    val posterUrl: String,
    val countryCodes: List<String>,
    val name: String,
    val genres: List<BookGenre>,
    val author: String,
    val description: String
) {
    companion object {
        fun default(category: Category=Category.GOOD) = Book(
            0L, category, 0, "", listOf(), "", listOf(), "", ""
        )
    }
}


enum class BookGenre {
    FICTION,
    SCIENCE_FICTION,
    THRILLER,
    HISTORY,
    HISTORICAL_FICTION,
    FANTASY,
    MEMOIR,
    SHORT_STORIES,
    HUMOR,
    BIOGRAPHY,
    SPIRITUALITY,
    TRAVEL_LITERATURE,
    MAGICAL_REALISM,
    WESTERN_FICTION,
    LITERATURE_REALISM,
    SOCIAL_SCIENCE,
    DYSTOPIAN_FICTION,
    PHILOSOPHY
}

