package dev.liinahamari.suggestions.impl.data.model

import com.google.gson.annotations.SerializedName
import dev.liinahamari.suggestions.api.model.books.RemoteOpenLibraryBook

data class SearchBookResponse(
    val start: Int? = null,
    @SerializedName("numFoundExact") var numFoundExact: Boolean? = null,
    @SerializedName("docs") var docs: ArrayList<RemoteOpenLibraryBook> = arrayListOf(),
    @SerializedName("num_found") var numFound: Int? = null,
    @SerializedName("q") var q: String? = null,
    @SerializedName("offset") var offset: String? = null
)
