package dev.liinahamari.impl.data.repos

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.impl.data.db.daos.MovieDao
import dev.liinahamari.impl.data.db.daos.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MoviesRepoImpl @Inject constructor(private val moviesDao: MovieDao) : MoviesRepo {
    override fun getAllMoviesByCategory(category: Category): Single<List<Movie>> = moviesDao.getAll(category)
        .toObservable()
        .map { it.map { it.toDomain() } }
        .firstOrError()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

interface MoviesRepo {
    fun getAllMoviesByCategory(category: Category): Single<List<Movie>>
}
