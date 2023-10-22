package dev.liinahamari.impl.domain.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.impl.data.repos.DocumentariesRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class GetDocumentariesUseCaseImpl @Inject constructor(private val documentariesRepo: DocumentariesRepo) :
    GetDocumentariesUseCase {
    override fun getAllDocumentaries(category: Category): Single<List<Documentary>> =
        documentariesRepo.getAllDocumentariesByCategory(category)

    override fun findById(category: Category, id: Long): Single<Documentary> = documentariesRepo.findById(category, id)
}
