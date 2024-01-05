package dev.liinahamari.suggestions.impl.data.model

import com.google.gson.annotations.SerializedName
import dev.liinahamari.suggestions.api.model.MovieGenre

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<MovieGenre>
)
