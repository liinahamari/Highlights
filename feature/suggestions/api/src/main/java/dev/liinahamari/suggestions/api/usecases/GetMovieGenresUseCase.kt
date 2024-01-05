package dev.liinahamari.suggestions.api.usecases

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface GetMovieGenresUseCase {
    fun syncMovieGenres(): Completable
    fun getMovieGenresByIds(ids: List<Int>): Single<List<dev.liinahamari.api.domain.entities.MovieGenre>>
}
