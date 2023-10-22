package dev.liinahamari.list_ui.single_entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EntityCategory : Parcelable {
    GOOD, SO_SO, WISH_LIST
}
