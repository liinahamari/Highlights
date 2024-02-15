package dev.liinahamari.list_ui.entries_list

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.usecases.RestoreDatabase
import dev.liinahamari.api.domain.usecases.RestoreDatabaseUseCase
import dev.liinahamari.api.domain.usecases.SaveDatabase
import dev.liinahamari.api.domain.usecases.SaveDatabaseUseCase
import dev.liinahamari.core.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class EntriesViewModel @Inject constructor(
    private val saveDatabaseUseCase: SaveDatabaseUseCase,
    private val restoreDatabaseUseCase: RestoreDatabaseUseCase
) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val _successfulBackupEvent = SingleLiveEvent<String>()
    val successfulBackupEvent: LiveData<String> get() = _successfulBackupEvent
    private val _errorEvent = SingleLiveEvent<String>()
    val errorEvent: LiveData<String> get() = _errorEvent
    private val _restoreDatabase = SingleLiveEvent<Unit>()
    val restoreDatabase: LiveData<Unit> get() = _restoreDatabase

    fun saveDatabase(result: Uri?) {
        result ?: return

        disposable.add(saveDatabaseUseCase.saveDatabase(result).subscribe { it ->
            when (it) {
                SaveDatabase.Success -> _successfulBackupEvent.value = "Backup successful"
                SaveDatabase.Error -> _errorEvent.value = "Backup failed"
            }
        })
    }

    fun restoreDatabase(result: Uri?) {
        result ?: return

        disposable.add(restoreDatabaseUseCase.restoreDatabase(result).subscribe { it ->
            when (it) {
                RestoreDatabase.Success -> _restoreDatabase.call()
                RestoreDatabase.Error -> _errorEvent.value = "Import failed"
            }
        })
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}
