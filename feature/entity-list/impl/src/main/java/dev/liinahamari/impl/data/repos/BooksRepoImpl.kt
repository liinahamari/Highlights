package dev.liinahamari.impl.data.repos

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.models.toData
import dev.liinahamari.impl.data.db.daos.models.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class BooksRepoImpl @Inject constructor(private val bookDao: BookDao) : BooksRepo {
    override fun save(vararg books: Book): Completable = bookDao.insert(books.toData())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun getAllBooksByCategory(category: Category): Single<List<Book>> =
        bookDao.getAll(category)
            .toObservable()
            .map { it.map { it.toDomain() } }
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun delete(id: Long): Completable = bookDao.delete(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun findByIds(category: Category, ids: Set<Long>): Single<List<Book>> = bookDao.findByIds(category, ids)
        .map { it.map { it.toDomain() } }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun findById(category: Category, id: Long): Single<Book> = bookDao.findById(category, id)
        .map { it.toDomain() }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

interface BooksRepo {
    fun getAllBooksByCategory(category: Category): Single<List<Book>>
    fun save(vararg books: Book): Completable
    fun delete(id: Long): Completable
    fun findByIds(category: Category, ids: Set<Long>): Single<List<Book>>
    fun findById(category: Category, id: Long): Single<Book>
}
