package dev.liinahamari.api.domain.usecases.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import io.reactivex.rxjava3.core.Single

interface GetMoviesUseCase {
    fun getAllMovies(category: Category): Single<List<Movie>>
    fun findById(category: Category, id: Long): Single<Movie>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Movie>>
}
