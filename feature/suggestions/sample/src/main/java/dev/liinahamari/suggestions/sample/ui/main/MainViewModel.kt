package dev.liinahamari.suggestions.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.suggestions.MovieSuggestionsListFactory.getApi
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.suggestions.api.model.RemoteMovie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.PublishSubject
import java.net.UnknownHostException

class MainViewModel : ViewModel() {
    private var initSearch = false
    private val searchMovieUseCase by lazy { getApi().searchMovieUseCase }

    private val _searchMoviesEvent = SingleLiveEvent<GetRemoteMovies>()
    val searchMoviesEvent: LiveData<GetRemoteMovies> get() = _searchMoviesEvent

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    private val queryBridge = PublishSubject.create<String>()
    fun searchMovie(movieTitle: String) {
        if (movieTitle.isBlank()) return
        if (initSearch.not()) {
            disposable += Observable.defer { queryBridge.switchMapSingle(::searchForMovie) }
                .subscribeOn(io())
                .observeOn(mainThread())
                .doOnNext { result -> _searchMoviesEvent.value = result }
                .subscribe()
            initSearch = true
        }
        queryBridge.onNext(movieTitle)
    }

    private fun searchForMovie(query: String): Single<GetRemoteMovies> = searchMovieUseCase.search(query)
        .map<GetRemoteMovies>(GetRemoteMovies::Success)
        .onErrorReturn {
            when (it) {
                is UnknownHostException -> GetRemoteMovies.Error.NoInternetError
                else -> GetRemoteMovies.Error.CommonError
            }
        }
}

sealed interface GetRemoteMovies {
    data class Success(val movies: List<RemoteMovie>) : GetRemoteMovies
    sealed interface Error : GetRemoteMovies {
        object CommonError : Error
        object NoInternetError : Error
    }
}
