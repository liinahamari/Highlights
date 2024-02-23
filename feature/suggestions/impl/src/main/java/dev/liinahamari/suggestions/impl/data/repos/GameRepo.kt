package dev.liinahamari.suggestions.impl.data.repos

import dev.liinahamari.suggestions.api.model.RemoteGame
import dev.liinahamari.suggestions.impl.data.apis.SearchGameApi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface GameRepo {
    fun search(searchParams: String): Single<List<RemoteGame>>
}

class GameRepoImpl @Inject constructor(
    private val api: SearchGameApi
) : GameRepo {
    override fun search(searchParams: String): Single<List<RemoteGame>> =
        api.searchGameByTitle(title = searchParams)
            .map { it.results.orEmpty() }
            .flatMapObservable { Observable.fromIterable(it) }
            .flatMapSingle { remoteGame ->
                api.searchGameById(remoteGame.id!!)
                    .map { remoteGame.copy(description = it.description ?: it.descriptionRaw) }
            }
            .toList()

}
