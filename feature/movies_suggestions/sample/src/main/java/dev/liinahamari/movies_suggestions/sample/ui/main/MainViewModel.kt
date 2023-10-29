package dev.liinahamari.movies_suggestions.sample.ui.main

import androidx.lifecycle.ViewModel
import com.example.movies_suggestions.MovieSuggestionsListFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    override fun onCleared() = disposable.clear()
    fun searchForMovie(text: String) {
        disposable += MovieSuggestionsListFactory.getApi().searchMovieUseCase.search(text)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}
