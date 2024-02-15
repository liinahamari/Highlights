package dev.liinahamari.impl.domain

import android.content.Context
import android.net.Uri
import dev.liinahamari.api.DATABASE_NAME
import dev.liinahamari.api.domain.usecases.SaveDatabase
import dev.liinahamari.api.domain.usecases.SaveDatabaseUseCase
import dev.liinahamari.impl.data.db.EntriesDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class SaveDatabaseUseCaseImpl @Inject constructor(private val context: Context, private val db: EntriesDatabase) : SaveDatabaseUseCase {
    override fun saveDatabase(saveFileUri: Uri): Single<SaveDatabase> = Single.just(saveFileUri)
        .doOnSuccess { uri ->
            db.close()
            File(context.getDatabasePath(DATABASE_NAME).absolutePath)
                .inputStream()
                .buffered()
                .use { context.contentResolver.openOutputStream(uri)!!.use(it::copyTo) }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(mainThread())
        .map<SaveDatabase> { SaveDatabase.Success }
        .onErrorReturn { SaveDatabase.Error }
}
