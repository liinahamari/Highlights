package dev.liinahamari.suggestions.impl.data.apis

import dev.liinahamari.suggestions.impl.data.model.SearchGameResponse
import io.reactivex.rxjava3.core.Single

interface SearchGameApi {
    fun searchGameByTitle(title:String): Single<SearchGameResponse>
}
