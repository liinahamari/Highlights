package dev.liinahamari.suggestions.impl.data.model

import com.google.gson.annotations.SerializedName

data class SearchGameByIdResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("name_original") val originalName: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("description_raw") val descriptionRaw: String? = null
) 
