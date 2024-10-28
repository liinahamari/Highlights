package dev.liinahamari.api.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize //fixme domain model in suggestions-ui module
data class Short(
    val localId: Long = 0L,
    val category: Category,
    val tmdbId: Int,
    val tmdbUrl: String?,
    val releaseYear: Int,
    val posterUrl: String?,
    val productionCountries: List<Country>,
    val title: String,
    val description: String
) : Parcelable {
    companion object {
        fun default(category: Category = Category.GOOD) = Short(0L, category, 0, "", 0, "", listOf(), "", "")
    }
}
