package dev.liinahamari.api.domain.usecases.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.entities.DocumentaryUi
import io.reactivex.rxjava3.core.Single

interface GetDocumentariesUseCase {
    fun getAllDocumentaries(category: Category): Single<GetAllDocumentariesResult>
    fun filter(category: Category, countryCode: String): Single<List<Documentary>>
    fun findById(category: Category, id: Long): Single<Documentary>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Documentary>>
}

sealed interface GetAllDocumentariesResult {
    data object EmptyList : GetAllDocumentariesResult
    data class Success(val data: List<DocumentaryUi>) : GetAllDocumentariesResult
    data object Error : GetAllDocumentariesResult
}
