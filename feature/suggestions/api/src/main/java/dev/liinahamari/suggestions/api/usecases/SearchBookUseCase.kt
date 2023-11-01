package dev.liinahamari.suggestions.api.usecases

import dev.liinahamari.suggestions.api.model.RemoteBook
import io.reactivex.rxjava3.core.Single

interface SearchBookUseCase {
    fun search(searchParams: String): Single<List<RemoteBook>>
}
