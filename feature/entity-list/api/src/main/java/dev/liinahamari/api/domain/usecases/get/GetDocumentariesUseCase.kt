package dev.liinahamari.api.domain.usecases.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.entities.EntryUi
import io.reactivex.rxjava3.core.Single

interface GetDocumentariesUseCase {
    fun getAllDocumentaries(category: Category): Single<GetAllDocumentariesResult>
    fun filter(category: Category, countryCode: String): Single<List<Documentary>>
    fun findById(category: Category, id: Long): Single<Documentary>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Documentary>>
}

sealed interface GetAllDocumentariesResult {
    object EmptyList: GetAllDocumentariesResult
    data class Success(val data: List<EntryUi>): GetAllDocumentariesResult
    object Error: GetAllDocumentariesResult
}
