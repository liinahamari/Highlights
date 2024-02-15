package dev.liinahamari.impl.domain

import android.content.Context
import android.net.Uri
import dev.liinahamari.api.DATABASE_NAME
import dev.liinahamari.api.domain.usecases.RestoreDatabase
import dev.liinahamari.api.domain.usecases.RestoreDatabaseUseCase
import dev.liinahamari.impl.data.db.EntriesDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class RestoreDatabaseUseCaseImpl @Inject constructor(private val context: Context, private val db: EntriesDatabase) :
    RestoreDatabaseUseCase {
    override fun restoreDatabase(restoreUri: Uri): Single<RestoreDatabase> = Single.just(restoreUri)
        .doOnSuccess {
            db.close()
            File("/data/data/dev.liinahamari.highlights/", "databases").mkdir()
            context.contentResolver.openInputStream(it)!!
                .use { input -> context.getDatabasePath(DATABASE_NAME).outputStream().use(input::copyTo) }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map<RestoreDatabase> { RestoreDatabase.Success }
        .onErrorReturn { RestoreDatabase.Error }
}
