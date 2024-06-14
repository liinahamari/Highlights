package dev.liinahamari.impl.domain.get

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.entities.EntryUi
import dev.liinahamari.api.domain.usecases.get.GetAllBooksResult
import dev.liinahamari.api.domain.usecases.get.GetBooksUseCase
import dev.liinahamari.impl.data.repos.BooksRepo
import io.reactivex.rxjava3.core.Single
import java.util.Locale
import javax.inject.Inject

class GetBooksUseCaseImpl @Inject constructor(private val booksRepo: BooksRepo) : GetBooksUseCase {
    override fun filter(category: Category, countryCode: String): Single<List<Book>> =
        booksRepo.filterByCountry(category, countryCode)

    override fun getAllBooks(category: Category): Single<GetAllBooksResult> = booksRepo.getAllBooksByCategory(category)
        .map {
            if (it.isEmpty().not()) {
                GetAllBooksResult.Success(it.map {
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
                GetAllBooksResult.EmptyList
            }
        }

    override fun findById(category: Category, id: Long): Single<Book> = booksRepo.findById(category, id)
    override fun findByIds(category: Category, ids: Set<Long>): Single<List<Book>> = booksRepo.findByIds(category, ids)
}
