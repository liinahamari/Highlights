package dev.liinahamari.highlights.db.daos

import dev.liinahamari.highlights.ui.main.EntityCategory

interface Entry {
    val category: EntityCategory
    val year: Int
    val posterUrl: String
}
