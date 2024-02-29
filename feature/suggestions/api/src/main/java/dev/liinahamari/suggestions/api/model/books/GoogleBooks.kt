package dev.liinahamari.suggestions.api.model.books

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.BookGenre
import dev.liinahamari.api.domain.entities.Category

data class BooksResource(
    val remoteGoogleBooks: List<RemoteGoogleBook?>?,
    val kind: String?,
    val totalItems: Int?
)

data class RemoteGoogleBook(
    val accessInfo: AccessInfo?,
    val etag: String?,
    val id: String?,
    val kind: String?,
    val saleInfo: SaleInfo?,
    val searchInfo: SearchInfo?,
    val selfLink: String?,
    val volumeInfo: VolumeInfo?
)

data class AccessInfo(
    val accessViewStatus: String?,
    val country: String?,
    val embeddable: Boolean?,
    val epub: Epub?,
    val pdf: Pdf?,
    val publicDomain: Boolean?,
    val quoteSharingAllowed: Boolean?,
    val textToSpeechPermission: String?,
    val viewability: String?,
    val webReaderLink: String?
)

data class Pdf(
    val acsTokenLink: String?,
    val isAvailable: Boolean?
)

data class SaleInfo(
    val country: String?,
    val isEbook: Boolean?,
    val saleability: String?
)

data class SearchInfo(
    val textSnippet: String?
)

data class VolumeInfo(
    val allowAnonLogging: Boolean?,
    val authors: List<String?>?,
    val averageRating: Double?,
    val canonicalVolumeLink: String?,
    val categories: List<String?>?,
    val contentVersion: String?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val industryIdentifiers: List<IndustryIdentifier?>?,
    val infoLink: String?,
    val language: String?,
    val maturityRating: String?,
    val pageCount: Int?,
    val panelizationSummary: PanelizationSummary?,
    val previewLink: String?,
    val printType: String?,
    val publishedDate: String?,
    val publisher: String?,
    val ratingsCount: Int?,
    val readingModes: ReadingModes?,
    val subtitle: String?,
    val title: String?
)

data class ImageLinks(
    val smallThumbnail: String?,
    val thumbnail: String?
) {
    constructor() : this("", "")
}

data class PanelizationSummary(
    val containsEpubBubbles: Boolean?,
    val containsImageBubbles: Boolean?
)

data class IndustryIdentifier(
    val identifier: String?,
    val type: String?
) {
    constructor() : this("", "")
}

data class ReadingModes(
    val image: Boolean?,
    val text: Boolean?
)

data class Epub(
    val acsTokenLink: String?,
    val isAvailable: Boolean?
)

fun RemoteGoogleBook.toDomain(category: Category, genres: List<BookGenre> = listOf()): Book =
    Book(
        category = category,
        countryCodes = emptyList(),
        genres = genres,
        name = volumeInfo!!.title!!,
        description = volumeInfo.description?:"",
        posterUrl = volumeInfo.imageLinks?.thumbnail ?: "",
        year = volumeInfo.publishedDate?.substring(0, 4)?.toInt() ?: 0,
        author = volumeInfo.authors?.joinToString() ?: ""
    )
