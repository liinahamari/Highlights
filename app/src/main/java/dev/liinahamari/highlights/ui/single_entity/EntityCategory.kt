package dev.liinahamari.highlights.ui.single_entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EntityCategory : Parcelable {
    GOOD, SO_SO, WISHLIST
}
