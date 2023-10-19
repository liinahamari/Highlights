package dev.liinahamari.api.domain.usecases

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import io.reactivex.rxjava3.core.Single

interface GetAllMoviesUseCase {
    fun getAllMovies(category: Category): Single<List<Movie>>
}
