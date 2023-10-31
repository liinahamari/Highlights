package dev.liinahamari.suggestions.impl.data.apis

import dev.liinahamari.suggestions.impl.BuildConfig
import dev.liinahamari.suggestions.impl.data.model.MoviesSearchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchMovieApi {
    @GET("search/multi")
    fun search(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en"
    ): Single<MoviesSearchResponse>
}
