package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.usecases.GetAllDocumentariesUseCase
import dev.liinahamari.impl.data.repos.DocumentariesRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class GetAllDocumentariesUseCaseImpl @Inject constructor(private val documentariesRepo: DocumentariesRepo) :
    GetAllDocumentariesUseCase {
    override fun getAllDocumentaries(category: Category): Single<List<Documentary>> =
        documentariesRepo.getAllDocumentariesByCategory(category)
}
