package dev.liinahamari.suggestions_ui.movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.suggestions.MovieSuggestionsListFactory
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.suggestions.api.MovieSuggestionsDependencies
import dev.liinahamari.suggestions.api.model.RemoteMovie
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

    private val api by lazy { MovieSuggestionsListFactory.getApi(object : MovieSuggestionsDependencies {
            override val application: Application
                get() = application
        })
    }

    protected val searchMovieUseCase by lazy { api.searchMovieUseCase }
    private val movieGenresUseCase by lazy { api.getMovieGenresUseCase }

    fun searchForMovie(query: String, category: Category): Observable<GetRemoteMovies> =
        searchMovieUseCase.search(query)//TODO move to usecase
            .flatMapObservable { Observable.fromIterable(it) }
            .flatMapSingle { movie -> getMovieGenres(movie.genreIds.orEmpty()).map { movie.toDomain(category, it) } }
            .toList()
            .map<GetRemoteMovies>(GetRemoteMovies::Success)
            .onErrorReturn {
                when (it) {
                    is UnknownHostException -> GetRemoteMovies.Error.NoInternetError
                    else -> GetRemoteMovies.Error.CommonError
                }
            }
            .toObservable()
            .doOnError { it.printStackTrace() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(_searchMoviesEvent::setValue)

    private fun getMovieGenres(ids: List<Int>): Single<List<dev.liinahamari.api.domain.entities.MovieGenre>> =
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

        sealed interface Error : GetRemoteMovies {
            object CommonError : Error
            object NoInternetError : Error
        }
    }
}

fun RemoteMovie.toDomain(category: Category, genres: List<dev.liinahamari.api.domain.entities.MovieGenre>): Movie =
    Movie(
        id = 0, // fixme
        category = category,
        countryCodes = originCountry.orEmpty(),
        genres = genres,
        name = this.title!!,
        description = overview!!,
        posterUrl = "https://image.tmdb.org/t/p/w500${this.posterPath}",
        year = if (this.releaseDate.isNullOrBlank()) 0 else this.releaseDate?.substring(0, 4)!!.toInt()
    )
