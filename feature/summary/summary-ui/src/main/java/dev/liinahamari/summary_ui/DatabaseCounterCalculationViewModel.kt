package dev.liinahamari.summary_ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.DatabaseCounters
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.core.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class DatabaseCounterCalculationViewModel @Inject constructor(private val databaseCountersUseCase: DatabaseCountersUseCase) :
    ViewModel() {
    private val _emptyViewEvent = SingleLiveEvent<Unit>()
    val emptyViewEvent: LiveData<Unit> get() = _emptyViewEvent
    private val _chartDataEvent = SingleLiveEvent<DatabaseCounters.Success>()
    val chartDataEvent: LiveData<DatabaseCounters.Success> get() = _chartDataEvent
    private val _errorEvent = SingleLiveEvent<String>()
    val errorEvent: LiveData<String> get() = _errorEvent


    private val disposable = CompositeDisposable()
    override fun onCleared() = disposable.dispose()

    fun getDatabaseCounters() =
        disposable.add(databaseCountersUseCase.getAllDatabaseCounters().subscribe { counters ->
            when (counters) {
                is DatabaseCounters.Empty -> _emptyViewEvent.call()
                is DatabaseCounters.DatabaseCorruptionError -> _errorEvent.value = "Something wrong with database!"
                is DatabaseCounters.Success -> _chartDataEvent.value = counters
            }
        })
}
