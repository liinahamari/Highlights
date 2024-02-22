package dev.liinahamari.suggestions.api.model

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.BookGenre
import dev.liinahamari.api.domain.entities.Category

data class RemoteBook(
    val title: String,
    val isbn: List<String>,
    val authorKey: List<String>,
    val firstPublishYear: Int,
    val numberOfPagesMedian: Int,
    val authorName: List<String?>?,
    val authorAlternativeName: List<String>,
    val coverEditionKey: String,
    val subject: List<String>
)

fun RemoteBook.toDomain(category: Category, genres: List<BookGenre> = listOf()): Book =
    Book(
        category = category,
        countryCodes = emptyList(),
        genres = genres,
        name = this.title,
        description = "",
        posterUrl = "https://covers.openlibrary.org/b/olid/${this.coverEditionKey}-S.jpg",
        year = firstPublishYear,
        author = authorName?.first() ?: ""
    )
