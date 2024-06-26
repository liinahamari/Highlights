package dev.liinahamari.suggestions.impl.data.repos

import dev.liinahamari.suggestions.api.model.MovieDetailsResponse
import dev.liinahamari.suggestions.api.model.MovieGenre
import dev.liinahamari.suggestions.api.model.TmdbRemoteMovie
import dev.liinahamari.suggestions.impl.data.apis.SearchMovieApi
import dev.liinahamari.suggestions.impl.data.db.MovieGenreDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface MovieRepo {
    fun search(searchParams: String): Single<List<TmdbRemoteMovie>>
    fun getMovieDetails(remoteId: Long): Single<MovieDetailsResponse>
    fun getGenres(): Single<List<MovieGenre>>
    fun syncGenres(): Completable
}

class MovieRepoImpl @Inject constructor(
    private val api: SearchMovieApi, private val genreDao: MovieGenreDao
) : MovieRepo {
    override fun search(searchParams: String): Single<List<TmdbRemoteMovie>> =
        api.search(searchParams).map { it.results }

    override fun getMovieDetails(remoteId: Long): Single<MovieDetailsResponse> = api.getMovieDetails(remoteId)
    override fun syncGenres(): Completable = genreDao.count().filter { it == 0 }
        .flatMapSingle { api.getMovieGenres() }
        .map { response ->
            response.genres.map { genre ->
                dev.liinahamari.suggestions.impl.data.db.MovieGenre(id = genre.id, name = genre.name)
            }
        }.flatMapCompletable(genreDao::insert)

    override fun getGenres(): Single<List<MovieGenre>> = genreDao.getAll()
        .map { movieGenres -> movieGenres.map { movieGenre -> MovieGenre(id = movieGenre.id, name = movieGenre.name) } }
}
