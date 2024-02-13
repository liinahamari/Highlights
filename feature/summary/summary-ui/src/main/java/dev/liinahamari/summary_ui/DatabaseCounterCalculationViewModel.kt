package dev.liinahamari.summary_ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.api.domain.usecases.Entity
import dev.liinahamari.core.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
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

    private val _combinedChartData = SingleLiveEvent<List<Entity>>()
    val combinedChartData: LiveData<List<Entity>> get() = _combinedChartData


    private val disposable = CompositeDisposable()
    override fun onCleared() = disposable.dispose()

    fun getDatabaseCounters() {
        disposable.add(
            databaseCountersUseCase.getAllDatabaseCounters().subscribe { it ->
                it.entities.forEach {
                    when (it) {
                        is Entity.Books -> _booksAmount.value = it.counter.toInt().toString()
                        is Entity.Games -> _gamesAmount.value = it.counter.toInt().toString()
                        is Entity.Documentaries -> _documentariesAmount.value = it.counter.toInt().toString()
                        is Entity.Movies -> _moviesAmount.value = it.counter.toInt().toString()
                    }
                }
                _totalAmount.value = it.totalCounter
                _combinedChartData.value = it.entities
            })
    }
}
