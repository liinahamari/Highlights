package dev.liinahamari.impl.data.repos

import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.impl.data.db.daos.DocumentaryDao
import dev.liinahamari.impl.data.db.daos.models.toData
import dev.liinahamari.impl.data.db.daos.models.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DocumentariesRepoImpl @Inject constructor(private val documentaryDao: DocumentaryDao) : DocumentariesRepo {
    override fun save(vararg documentaries: Documentary): Completable =
        documentaryDao.insert(documentaries.toData())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun delete(id: Long): Completable = documentaryDao.delete(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun getAllDocumentariesByCategory(category: Category): Single<List<Documentary>> =
        documentaryDao.getAll(category)
            .toObservable()
            .map { it.map { it.toDomain() } }
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun findById(category: Category, id: Long): Single<Documentary> = documentaryDao.findById(category, id)
        .map { it.toDomain() }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun findByIds(category: Category, ids: Set<Long>): Single<List<Documentary>> =
        documentaryDao.findByIds(category, ids)
            .map { it.map { it.toDomain() } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

interface DocumentariesRepo {
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Documentary>>
    fun findById(category: Category, id: Long): Single<Documentary>
    fun getAllDocumentariesByCategory(category: Category): Single<List<Documentary>>
    fun save(vararg documentaries: Documentary): Completable
    fun delete(id: Long): Completable
}
