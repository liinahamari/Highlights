package dev.liinahamari.suggestions.impl.usecases

import dev.liinahamari.suggestions.api.usecases.GetMovieGenresUseCase
import dev.liinahamari.suggestions.impl.data.repos.MovieRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetMovieGenresUseCaseImpl @Inject constructor(private val movieRepo: MovieRepo) : GetMovieGenresUseCase {
    override fun syncMovieGenres(): Completable = movieRepo.syncGenres()

    //todo write tests for whitespaces and "_"
    override fun getMovieGenresByIds(ids: List<Int>): Single<List<dev.liinahamari.api.domain.entities.MovieGenre>> =
        movieRepo.getGenres()
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { it.id in ids }
            .toList()
            .map { remoteMovieGenres -> remoteMovieGenres.map { it.name.trim().lowercase().replace("_", " ") } }
            .map { remoteGenreNames ->
                dev.liinahamari.api.domain.entities.MovieGenre.values()
                    .filter { localGenreName ->
                        remoteGenreNames.any { localGenreName.toString().lowercase().contains(it) }
                    }
                    .sorted()
            }
}
