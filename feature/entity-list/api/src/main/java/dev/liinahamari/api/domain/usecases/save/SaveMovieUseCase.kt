package dev.liinahamari.api.domain.usecases.save

import dev.liinahamari.api.domain.entities.Movie
import io.reactivex.rxjava3.core.Completable

interface SaveMovieUseCase {
    fun saveMovie(movie: Movie): Completable
}
