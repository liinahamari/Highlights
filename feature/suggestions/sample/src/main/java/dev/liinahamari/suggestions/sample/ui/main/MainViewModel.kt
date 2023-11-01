package dev.liinahamari.suggestions.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.suggestions.MovieSuggestionsListFactory.getApi
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.suggestions.api.model.RemoteMovie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.PublishSubject
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit.SECONDS

class MainViewModel : ViewModel() {
    private val searchMovieUseCase by lazy { getApi().searchMovieUseCase }

    private val _searchMoviesEvent = SingleLiveEvent<GetRemoteMovies>()
    val searchMoviesEvent: LiveData<GetRemoteMovies> get() = _searchMoviesEvent

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    private val searchMovieSubject: Observer<String> = PublishSubject.create<String>().apply {
        disposable += flatMapSingle(searchMovieUseCase::search)
            .throttleFirst(1, SECONDS, io())
            .observeOn(mainThread())
            .map<GetRemoteMovies>(GetRemoteMovies::Success)
            .onErrorReturn {
                when (it) {
                    is UnknownHostException -> GetRemoteMovies.Error.NoInternetError
                    else -> GetRemoteMovies.Error.CommonError
                }
            }
            .doOnNext { result -> _searchMoviesEvent.value = result }
            .subscribe()
    }

    fun searchForMovie(query: String) {
        searchMovieSubject.onNext(query)
    }
}

sealed interface GetRemoteMovies {
    data class Success(val movies: List<RemoteMovie>) : GetRemoteMovies
    sealed interface Error : GetRemoteMovies {
        object CommonError : Error
        object NoInternetError : Error
    }
}
