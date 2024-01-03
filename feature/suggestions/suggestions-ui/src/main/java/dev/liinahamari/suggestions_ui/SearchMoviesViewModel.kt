package dev.liinahamari.suggestions_ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.suggestions.MovieSuggestionsListFactory
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.suggestions.api.model.RemoteMovie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.UnknownHostException

internal class SearchMoviesViewModel : ViewModel() {
    private val _searchMoviesEvent = SingleLiveEvent<GetRemoteMovies>()
    val searchMoviesResultEvent: LiveData<GetRemoteMovies> get() = _searchMoviesEvent

    private val searchMovieUseCase by lazy { MovieSuggestionsListFactory.getApi().searchMovieUseCase }

    fun searchForMovie(query: String): Single<GetRemoteMovies> = searchMovieUseCase.search(query)
        .map<GetRemoteMovies>(GetRemoteMovies::Success)
        .onErrorReturn {
            when (it) {
                is UnknownHostException -> GetRemoteMovies.Error.NoInternetError
                else -> GetRemoteMovies.Error.CommonError
            }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(_searchMoviesEvent::setValue)

    sealed interface GetRemoteMovies {
        data class Success(val movies: List<RemoteMovie>) : GetRemoteMovies
        sealed interface Error : GetRemoteMovies {
            object CommonError : Error
            object NoInternetError : Error
        }
    }
}
