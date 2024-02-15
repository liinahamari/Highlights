package dev.liinahamari.api.domain.usecases

import android.net.Uri
import io.reactivex.rxjava3.core.Single

interface RestoreDatabaseUseCase {
    fun restoreDatabase(pathToRestore: Uri): Single<RestoreDatabase>
}

sealed interface RestoreDatabase {
    object Success : RestoreDatabase
    object Error : RestoreDatabase
}
