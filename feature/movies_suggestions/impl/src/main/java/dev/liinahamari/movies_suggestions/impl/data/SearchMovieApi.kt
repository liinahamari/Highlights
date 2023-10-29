package dev.liinahamari.movies_suggestions.impl.data

import dev.liinahamari.movies_suggestions.impl.BuildConfig
import dev.liinahamari.movies_suggestions.impl.data.model.MultiSearchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchMovieApi {
    @GET("search/multi")
    fun multiSearch(
        @Query("query") searchParams: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en"
    ): Single<MultiSearchResponse>
}
