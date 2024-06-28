package dev.liinahamari.suggestions.api.model.books

import com.google.gson.annotations.SerializedName
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.BookGenre
import dev.liinahamari.api.domain.entities.Category

const val TITLE = "title"
const val ISBN = "isbn"
const val AUTHOR_KEY = "author_key"
const val FIRST_PUBLISH_YEAR = "first_publish_year"
const val NUMBER_OF_PAGES_MEDIAN = "number_of_pages_median"
const val AUTHOR_NAME = "author_name"
const val AUTHOR_ALTERNATIVE_NAME = "author_alternative_name"
const val COVER_EDITION_KEY = "cover_edition_key"
const val SUBJECT = "subject"

data class RemoteOpenLibraryBook(
    @SerializedName(TITLE)
    val title: String,
    @SerializedName(ISBN)
    val isbn: List<String>,
    @SerializedName(AUTHOR_KEY)
    val authorKey: List<String>,
    @SerializedName(FIRST_PUBLISH_YEAR)
    val firstPublishYear: Int,
    @SerializedName(NUMBER_OF_PAGES_MEDIAN)
    val numberOfPagesMedian: Int,
    @SerializedName(AUTHOR_NAME)
    val authorName: List<String?>?,
    @SerializedName(AUTHOR_ALTERNATIVE_NAME)
    val authorAlternativeName: List<String>,
    @SerializedName(COVER_EDITION_KEY)
    val coverEditionKey: String,
    @SerializedName(SUBJECT)
    val subject: List<String>
)

fun RemoteOpenLibraryBook.toDomain(category: Category, genres: List<BookGenre> = listOf()): Book =
    Book(
        category = category,
        countries = emptyList(),
        genres = genres,
        name = this.title,
        description = "",
        posterUrl = "https://covers.openlibrary.org/b/olid/${this.coverEditionKey}-S.jpg",
        year = firstPublishYear,
        author = authorName?.first() ?: ""
    )
