package dev.liinahamari.suggestions.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemoteMovie(
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("id")
    val remoteId: Long?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>?,
    @SerializedName("origin_country")
    val originCountry: List<String>?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_name")
    val originalName: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date", alternate = ["first_air_date"])
    val releaseDate: String?,
    @SerializedName("title", alternate = ["name"])
    val title: String?,
) : Parcelable
