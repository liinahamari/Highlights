package dev.liinahamari.impl.data.repos

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.models.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DocumentariesRepoImpl @Inject constructor(private val documentaryDao: DocumentaryDao) : DocumentariesRepo {
    override fun getAllDocumentariesByCategory(category: Category): Single<List<Documentary>> =
        documentaryDao.getAll(category)
            .toObservable()
            .map { it.map { it.toDomain() } }
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

interface DocumentariesRepo {
    fun getAllDocumentariesByCategory(category: Category): Single<List<Documentary>>
}
