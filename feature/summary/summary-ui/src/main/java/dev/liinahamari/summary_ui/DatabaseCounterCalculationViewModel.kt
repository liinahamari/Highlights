package dev.liinahamari.summary_ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.liinahamari.api.domain.entities.DatabaseCounters
import dev.liinahamari.api.domain.usecases.DatabaseCountersUseCase
import dev.liinahamari.core.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseCounterCalculationViewModel @Inject constructor(private val databaseCountersUseCase: DatabaseCountersUseCase) :
    ViewModel() {
    private val _emptyViewEvent = SingleLiveEvent<Unit>()
    val emptyViewEvent: LiveData<Unit> get() = _emptyViewEvent
    private val _chartDataEvent = SingleLiveEvent<DatabaseCounters.Success>()
    val chartDataEvent: LiveData<DatabaseCounters.Success> get() = _chartDataEvent
    private val _errorEvent = SingleLiveEvent<String>()
    val errorEvent: LiveData<String> get() = _errorEvent

    fun getDatabaseCounters() {
        println("dsadadadas 0 ${Thread.currentThread().name}")
        viewModelScope.launch(IO) {
            println("dsadadadas 1 ${Thread.currentThread().name}")
            val databaseCounters = databaseCountersUseCase.getAllDatabaseCounters()
            withContext(Main) {
                println("dsadadadas 2 ${Thread.currentThread().name}")
                when (databaseCounters) {
                    is DatabaseCounters.Empty -> _emptyViewEvent.call()
                    is DatabaseCounters.DatabaseCorruptionError -> _errorEvent.value = "Something wrong with database!"
                    is DatabaseCounters.Success -> _chartDataEvent.value = databaseCounters
                }
            }
        }
    }
}
