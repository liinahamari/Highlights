package dev.liinahamari.impl.data.repos

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.impl.data.db.daos.MovieDao
import dev.liinahamari.impl.data.db.daos.models.toData
import dev.liinahamari.impl.data.db.daos.models.toDomain
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MoviesRepoImpl @Inject constructor(private val moviesDao: MovieDao) : MoviesRepo {
    override fun getAllMoviesByCategory(category: Category): Single<List<Movie>> = moviesDao.getAll(category)
        .toObservable()
        .map { it.toDomain() }
        .firstOrError()

    override fun delete(id: Long): Completable = moviesDao.delete(id)

    override fun filterByCountry(category: Category, countryCode: String): Single<List<Movie>> =
        moviesDao.filterByCountry(category, countryCode)
            .map { it.toDomain() }

    override fun save(vararg movies: Movie): Completable = moviesDao.insert(movies.toData())
    override fun findById(category: Category, id: Long): Single<Movie> = moviesDao.findById(category, id)
        .map { it.toDomain() }

    override fun findByIds(category: Category, ids: Set<Long>): Single<List<Movie>> = moviesDao.findByIds(category, ids)
        .map { it.toDomain() }
}

interface MoviesRepo {
    fun getAllMoviesByCategory(category: Category): Single<List<Movie>>
    fun save(vararg movies: Movie): Completable
    fun delete(id: Long): Completable
    fun findById(category: Category, id: Long): Single<Movie>
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Movie>>
    fun filterByCountry(category: Category, countryCode: String): Single<List<Movie>>
}
