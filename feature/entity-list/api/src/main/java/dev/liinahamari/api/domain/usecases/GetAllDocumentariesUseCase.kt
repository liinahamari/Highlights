package dev.liinahamari.api.domain.usecases

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import io.reactivex.rxjava3.core.Single

interface GetAllDocumentariesUseCase {
    fun getAllDocumentaries(category: Category): Single<List<Documentary>>
}
