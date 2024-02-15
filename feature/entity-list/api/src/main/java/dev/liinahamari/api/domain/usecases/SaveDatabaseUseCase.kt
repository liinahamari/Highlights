package dev.liinahamari.api.domain.usecases

import android.net.Uri
import io.reactivex.rxjava3.core.Single

interface SaveDatabaseUseCase {
    fun saveDatabase(saveFileUri: Uri): Single<SaveDatabase>
}

sealed interface SaveDatabase {
    object Success : SaveDatabase
    object Error : SaveDatabase
}
