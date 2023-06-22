package dev.liinahamari.highlights.db.daos

import dev.liinahamari.highlights.ui.single_entity.EntityCategory

interface Entry {
    val category: EntityCategory
    val year: Int
    val posterUrl: String
    val countryCodes: Array<String>
}
