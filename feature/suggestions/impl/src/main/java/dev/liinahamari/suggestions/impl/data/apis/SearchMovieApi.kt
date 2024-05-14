package dev.liinahamari.suggestions.impl.data.apis

import dev.liinahamari.suggestions.impl.BuildConfig
import dev.liinahamari.suggestions.impl.data.model.GenreResponse
import dev.liinahamari.suggestions.impl.data.model.MoviesSearchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchMovieApi {
    @GET("search/movie")
    fun search(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en",
        @Query("include_adult") includeAdult: Boolean = false
    ): Single<MoviesSearchResponse>

    @GET("genre/movie/list")
    fun getMovieGenres(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en"
    ): Single<GenreResponse>
}
