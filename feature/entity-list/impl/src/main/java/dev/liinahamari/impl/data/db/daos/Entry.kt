package dev.liinahamari.impl.data.db.daos

import dev.liinahamari.api.domain.entities.Category

interface Entry {
    val category: Category
    val year: Int
    val posterUrl: String?
    val countryCodes: Array<String>
    val description: String
}
