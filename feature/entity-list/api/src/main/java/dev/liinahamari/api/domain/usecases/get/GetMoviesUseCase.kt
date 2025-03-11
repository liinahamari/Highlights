package dev.liinahamari.api.domain.usecases.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.api.domain.entities.MovieUi
import io.reactivex.rxjava3.core.Single

interface GetMoviesUseCase {
    fun getAllMovies(category: Category): Single<GetAllMoviesResult>
    fun filter(category: Category, countryCode: String): Single<List<Movie>>
    fun findById(category: Category, id: Long): Single<Movie>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Movie>>
}

sealed interface GetAllMoviesResult {
    data object EmptyList : GetAllMoviesResult
    data class Success(val data: List<MovieUi>) : GetAllMoviesResult
    data object Error : GetAllMoviesResult
}
