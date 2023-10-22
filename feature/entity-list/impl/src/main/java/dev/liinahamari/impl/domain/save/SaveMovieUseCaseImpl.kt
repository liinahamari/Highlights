package dev.liinahamari.impl.domain.save

import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.api.domain.usecases.save.SaveMovieUseCase
import dev.liinahamari.impl.data.repos.MoviesRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SaveMovieUseCaseImpl @Inject constructor(private val moviesRepo: MoviesRepo) : SaveMovieUseCase {
    override fun saveMovie(movie: Movie): Completable = moviesRepo.save(movie)
}
