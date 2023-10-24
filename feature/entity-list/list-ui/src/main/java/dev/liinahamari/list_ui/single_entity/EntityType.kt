package dev.liinahamari.list_ui.single_entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EntityType: Parcelable {
    BOOK, GAME, DOCUMENTARY, MOVIE
}
