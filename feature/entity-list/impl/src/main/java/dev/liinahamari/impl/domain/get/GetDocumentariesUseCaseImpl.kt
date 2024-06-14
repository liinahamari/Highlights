package dev.liinahamari.impl.domain.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.entities.EntryUi
import dev.liinahamari.api.domain.usecases.get.GetAllDocumentariesResult
import dev.liinahamari.api.domain.usecases.get.GetDocumentariesUseCase
import dev.liinahamari.impl.data.repos.DocumentariesRepo
import io.reactivex.rxjava3.core.Single
import java.util.Locale
import javax.inject.Inject

internal class GetDocumentariesUseCaseImpl @Inject constructor(private val documentariesRepo: DocumentariesRepo) :
    GetDocumentariesUseCase {
    override fun filter(category: Category, countryCode: String): Single<List<Documentary>> =
        documentariesRepo.filterByCountry(category, countryCode)

    override fun getAllDocumentaries(category: Category): Single<GetAllDocumentariesResult> =
        documentariesRepo.getAllDocumentariesByCategory(category)
            .map {
                if (it.isEmpty().not()) {
                    GetAllDocumentariesResult.Success(it.map {
                        EntryUi(
                            it.id,
                            title = it.name,
                            description = it.description,
                            genres = "",
                            countries = it.countryCodes.map { Locale("", it).displayCountry },
                            url = it.posterUrl,
                            year = it.year,
                            clazz = Documentary::class.java
                        )
                    })
                } else {
                    GetAllDocumentariesResult.EmptyList
                }
            }
            .onErrorReturn { GetAllDocumentariesResult.Error }

    override fun findByIds(category: Category, ids: Set<Long>): Single<List<Documentary>> =
        documentariesRepo.findByIds(category, ids)

    override fun findById(category: Category, id: Long): Single<Documentary> = documentariesRepo.findById(category, id)
}
