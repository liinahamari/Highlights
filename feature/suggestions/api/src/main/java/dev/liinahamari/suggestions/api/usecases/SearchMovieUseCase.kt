package dev.liinahamari.suggestions.api.usecases

import dev.liinahamari.suggestions.api.model.RemoteMovie
import io.reactivex.rxjava3.core.Single

interface SearchMovieUseCase {
    fun search(searchParams: String): Single<List<RemoteMovie>>
}
