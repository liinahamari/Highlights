package dev.liinahamari.suggestions.api.usecases

import dev.liinahamari.suggestions.api.model.RemoteGame
import io.reactivex.rxjava3.core.Single

interface SearchGameUseCase {
    fun search(searchParams: String): Single<List<RemoteGame>>
}
