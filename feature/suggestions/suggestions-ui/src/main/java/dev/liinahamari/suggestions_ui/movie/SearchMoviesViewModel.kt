package dev.liinahamari.suggestions_ui.movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.suggestions.MovieSuggestionsListFactory
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Country
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.api.domain.entities.MovieGenre
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.suggestions.api.MovieSuggestionsDependencies
import dev.liinahamari.suggestions.api.model.TmdbRemoteMovie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.UnknownHostException

open class SearchMoviesViewModel(application: Application) : AndroidViewModel(application) {
    private val disposable = CompositeDisposable()
    protected val _searchMoviesEvent = SingleLiveEvent<GetRemoteMovies>()
    val searchMoviesResultEvent: LiveData<GetRemoteMovies> get() = _searchMoviesEvent

    private val api by lazy {
        MovieSuggestionsListFactory.getApi(object : MovieSuggestionsDependencies {
            override val application: Application
                get() = application
        })
    }

    protected val searchMovieUseCase by lazy { api.searchMovieUseCase }
    private val movieGenresUseCase by lazy { api.getMovieGenresUseCase }

    fun searchForMovie(query: String, category: Category): Observable<GetRemoteMovies> =
        searchMovieUseCase.search(query)//TODO move to usecase
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { (it.posterPath.isNullOrBlank() || it.posterPath == "null").not() && it.releaseDate != "0" }
            .flatMapSingle { movie ->
                getMovieGenres(movie.genreIds.orEmpty())
                    .map { movie.toDomain(category, it) }
                    .flatMap { mov ->
                        searchMovieUseCase.getMovieDetails(movie.remoteId!!)
                            .map { response ->
                                mov.copy(
                                    productionCountries = response.productionCountries.map { Country(iso = it.iso31661!!, name = it.name!!) },
                                    genres = MovieGenre.values()
                                        .filter { localGenreName ->
                                            response.genres.map { it.name }.filter { it.isNullOrBlank().not() }.any {
                                                localGenreName.toString().lowercase().contains(it!!, true)
                                            }
                                        }
                                        .sorted()
                                )
                            }
                    }
            }
            .toList()
            .toObservable()
            .map<GetRemoteMovies>(GetRemoteMovies::Success)
            .startWithItem(GetRemoteMovies.Loading)
            .onErrorReturn {
                when (it) {
                    is UnknownHostException -> GetRemoteMovies.Error.NoInternetError
                    else -> GetRemoteMovies.Error.CommonError
                }
            }
            .doOnError { it.printStackTrace() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(_searchMoviesEvent::setValue)

    private fun getMovieGenres(ids: List<Int>): Single<List<MovieGenre>> =
        movieGenresUseCase.getMovieGenresByIds(ids)
            .subscribeOn(Schedulers.io())

    fun fetchMovieGenres() {
        disposable.add(
            movieGenresUseCase.syncMovieGenres()
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    sealed interface GetRemoteMovies {
        data class Success(val movies: List<Movie>) : GetRemoteMovies
        data object Loading: GetRemoteMovies
        sealed interface Error : GetRemoteMovies {
            data object CommonError : Error
            data object NoInternetError : Error
        }
    }
}

fun TmdbRemoteMovie.toDomain(category: Category, genres: List<MovieGenre> = emptyList()): Movie =
    Movie(
        localId = 0, // fixme
        tmdbUrl = remoteId?.let { "https://www.themoviedb.org/movie/$it" },
        tmdbId = remoteId!!.toInt(),
        category = category,
        productionCountries = emptyList(),
        genres = genres,
        title = this.title!!,
        description = overview!!,
        posterUrl = "https://image.tmdb.org/t/p/w500${this.posterPath}",
        releaseYear = if (this.releaseDate.isNullOrBlank()) 0 else this.releaseDate?.substring(0, 4)!!.toInt()
    )
