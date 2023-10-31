package dev.liinahamari.suggestions.impl.usecases

import dev.liinahamari.suggestions.api.SearchBookUseCase
import dev.liinahamari.suggestions.api.model.RemoteBook
import dev.liinahamari.suggestions.impl.data.repos.BookRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchBookUseCaseImpl @Inject constructor(private val bookRepo: BookRepo) : SearchBookUseCase {
    override fun search(searchParams: String): Single<List<RemoteBook>> = bookRepo.search(searchParams)
}
