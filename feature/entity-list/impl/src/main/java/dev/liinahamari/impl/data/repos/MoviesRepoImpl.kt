package dev.liinahamari.impl.data.repos

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.impl.data.db.daos.MovieDao
import dev.liinahamari.impl.data.db.daos.models.toData
import dev.liinahamari.impl.data.db.daos.models.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
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

    override fun delete(id: Long): Completable = moviesDao.delete(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun save(movie: Movie): Completable = moviesDao.insert(movie.toData())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun findById(category: Category, id: Long): Single<Movie> = moviesDao.findById(category, id)
        .map { it.toDomain() }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

interface MoviesRepo {
    fun getAllMoviesByCategory(category: Category): Single<List<Movie>>
    fun save(movie: Movie): Completable
    fun delete(id: Long): Completable
    fun findById(category: Category, id: Long): Single<Movie>
}
