package dev.liinahamari.highlights.ui.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EntityCategory : Parcelable {
    GOOD, SO_SO, WISHLIST
}
@Parcelize
enum class EntityType : Parcelable {
    BOOK, MOVIE, DOCUMENTARY, GAME
}
