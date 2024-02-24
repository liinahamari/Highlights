package dev.liinahamari.api.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Category(val emoji: String) : Parcelable {
    GOOD("👍"), SO_SO("👎"), WISHLIST("🌠")
}
