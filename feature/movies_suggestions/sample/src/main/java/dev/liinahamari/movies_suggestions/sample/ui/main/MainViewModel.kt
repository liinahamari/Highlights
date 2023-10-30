package dev.liinahamari.movies_suggestions.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movies_suggestions.MovieSuggestionsListFactory.getApi
import dev.liinahamari.core.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.UnknownHostException

class MainViewModel : ViewModel() {
    private val searchMovieUseCase by lazy { getApi().searchMovieUseCase }

    private val _searchMoviesEvent = SingleLiveEvent<GetRemoteMovies>()
    val searchMoviesEvent: LiveData<GetRemoteMovies> get() = _searchMoviesEvent

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun searchForMovie(query: String) {
        disposable += searchMovieUseCase.search(query)
            .concatMap { Single.just(it.map { it.title }) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<GetRemoteMovies> { GetRemoteMovies.Success(it) }
            .onErrorReturn {
                when (it) {
                    is UnknownHostException -> GetRemoteMovies.Error.NoInternetError
                    else -> GetRemoteMovies.Error.CommonError
                }
            }
            .subscribe { result -> _searchMoviesEvent.value = result }
    }
}

sealed interface GetRemoteMovies {
    data class Success(val titles: List<String>) : GetRemoteMovies
    sealed interface Error : GetRemoteMovies {
        object CommonError : Error
        object NoInternetError : Error
    }
}
