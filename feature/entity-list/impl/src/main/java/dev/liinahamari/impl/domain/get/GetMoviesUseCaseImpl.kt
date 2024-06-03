package dev.liinahamari.impl.domain.get

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.api.domain.usecases.get.GetMoviesUseCase
import dev.liinahamari.impl.data.repos.MoviesRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetMoviesUseCaseImpl @Inject constructor(private val moviesRepo: MoviesRepo) : GetMoviesUseCase {
    override fun filter(category: Category, countryCode: String): Single<List<Movie>> =
        moviesRepo.filterByCountry(category, countryCode)

    override fun getAllMovies(category: Category): Single<List<Movie>> = moviesRepo.getAllMoviesByCategory(category)
    override fun findById(category: Category, id: Long): Single<Movie> = moviesRepo.findById(category, id)
    override fun findByIds(category: Category, ids: Set<Long>): Single<List<Movie>> =
        moviesRepo.findByIds(category, ids)
}
