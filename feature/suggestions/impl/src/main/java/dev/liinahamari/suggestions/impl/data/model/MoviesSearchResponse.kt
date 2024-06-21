package dev.liinahamari.suggestions.impl.data.model

import com.google.gson.annotations.SerializedName
import dev.liinahamari.suggestions.api.model.TmdbRemoteMovie

class MoviesSearchResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<TmdbRemoteMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
