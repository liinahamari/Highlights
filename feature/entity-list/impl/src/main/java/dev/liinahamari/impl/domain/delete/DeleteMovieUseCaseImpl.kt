package dev.liinahamari.impl.domain.delete

import dev.liinahamari.api.domain.usecases.delete.DeleteMovieUseCase
import dev.liinahamari.impl.data.repos.MoviesRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteMovieUseCaseImpl @Inject constructor(private val moviesRepo: MoviesRepo) : DeleteMovieUseCase {
    override fun deleteMovie(id: Long): Completable = moviesRepo.delete(id)
}
