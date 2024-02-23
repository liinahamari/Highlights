package dev.liinahamari.suggestions.impl.data.apis

import dev.liinahamari.suggestions.api.model.SearchGameResponse
import dev.liinahamari.suggestions.impl.BuildConfig
import dev.liinahamari.suggestions.impl.data.model.SearchGameByIdResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchGameApi {
    @GET("games")
    fun searchGameByTitle(
        @Query("key") key: String = BuildConfig.RAWG_IO_API_KEY,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("search") title: String
    ): Single<SearchGameResponse>

    @GET("games/{id}")
    fun searchGameById(
        @Path("id") id: Int,
        @Query("key") key: String = BuildConfig.RAWG_IO_API_KEY
    ): Single<SearchGameByIdResponse>
}
