package dev.liinahamari.api.domain.usecases.delete

import io.reactivex.rxjava3.core.Completable

interface DeleteMovieUseCase {
    fun deleteMovie(id: Long): Completable
}
