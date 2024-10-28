package dev.liinahamari.suggestions.api.model

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("genres") var genres: ArrayList<Genres> = arrayListOf(),
    @SerializedName("origin_country") var originCountry: ArrayList<String> = arrayListOf(),
    @SerializedName("original_title") var originalTitle: String? = null,
    @SerializedName("production_countries") var productionCountries: ArrayList<ProductionCountries> = arrayListOf(),
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("imdb_id") var imdbId: String? = null,
    @SerializedName("poster_path") var posterPath: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("runtime") var runtime: Int? = null,
)
data class ProductionCountries(
    @SerializedName("iso_3166_1") var iso31661: String? = null,
    @SerializedName("name") var name: String? = null
)
data class SpokenLanguages(
    @SerializedName("english_name") var englishName: String? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("name") var name: String? = null
)
data class Genres(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null
)

