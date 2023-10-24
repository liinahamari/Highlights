package dev.liinahamari.api.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Category: Parcelable {
    GOOD, SO_SO, WISHLIST
}
