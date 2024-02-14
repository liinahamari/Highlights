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
    private val _combinedChartData = SingleLiveEvent<DatabaseCounters>()
    val combinedChartData: LiveData<DatabaseCounters> get() = _combinedChartData


    private val disposable = CompositeDisposable()
    override fun onCleared() = disposable.dispose()

    fun getDatabaseCounters() =
        disposable.add(databaseCountersUseCase.getAllDatabaseCounters().subscribe(_combinedChartData::setValue))
}
