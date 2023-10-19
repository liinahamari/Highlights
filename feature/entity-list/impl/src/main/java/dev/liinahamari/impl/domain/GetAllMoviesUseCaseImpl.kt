package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.api.domain.usecases.GetAllMoviesUseCase
import dev.liinahamari.impl.data.repos.MoviesRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetAllMoviesUseCaseImpl @Inject constructor(private val moviesRepo: MoviesRepo) : GetAllMoviesUseCase {
    override fun getAllMovies(category: Category): Single<List<Movie>> = moviesRepo.getAllMoviesByCategory(category)
}
