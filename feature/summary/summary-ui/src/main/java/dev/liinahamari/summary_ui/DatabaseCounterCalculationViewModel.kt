package dev.liinahamari.summary_ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.core.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DatabaseCounterCalculationViewModel @Inject constructor(private val databaseCountersUseCase: DatabaseCountersUseCase) :
    ViewModel() {
    private val _totalAmount = SingleLiveEvent<String>()
    val totalAmount: LiveData<String> get() = _totalAmount
    private val _moviesAmount = SingleLiveEvent<String>()
    val moviesAmount: LiveData<String> get() = _moviesAmount
    private val _documentariesAmount = SingleLiveEvent<String>()
    val documentariesAmount: LiveData<String> get() = _documentariesAmount
    private val _booksAmount = SingleLiveEvent<String>()
    val booksAmount: LiveData<String> get() = _booksAmount
    private val _gamesAmount = SingleLiveEvent<String>()
    val gamesAmount: LiveData<String> get() = _gamesAmount

    private val disposable = CompositeDisposable()
    fun getTotalAmount() {
        disposable.add(databaseCountersUseCase.getTotalAmount().map { it.toString() }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it -> _totalAmount.value = it })
    }

    fun getMoviesAmount() {
        disposable.add(databaseCountersUseCase.getMoviesAmount().map { it.toString() }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it -> _moviesAmount.value = it })
    }

    fun getDocumentariesAmount() {
        disposable.add(databaseCountersUseCase.getDocumentariesAmount().map { it.toString() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it -> _documentariesAmount.value = it })
    }

    fun getBooksAmount() {
        disposable.add(databaseCountersUseCase.getBooksAmount().map { it.toString() }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it -> _booksAmount.value = it })
    }

    fun getGamesAmount() {
        disposable.add(databaseCountersUseCase.getGamesAmount().map { it.toString() }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it -> _gamesAmount.value = it })
    }

    override fun onCleared() = disposable.dispose()
}
