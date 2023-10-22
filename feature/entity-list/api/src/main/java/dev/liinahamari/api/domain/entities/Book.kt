package dev.liinahamari.api.domain.entities

data class Book(
    val id: Long,
    val category: Category,
    val year: Int,
    val posterUrl: String,
    val countryCodes: List<String>,
    val name: String,
    val genres: List<BookGenre>,
    val author: String
) {
    companion object {
        fun default(category: Category) = Book(
            0L, category, 0, "", listOf(), "", listOf(), ""
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

