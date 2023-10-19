package dev.liinahamari.impl.data.repos

import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.impl.data.db.daos.BookDao
import dev.liinahamari.impl.data.db.daos.models.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class BooksRepoImpl @Inject constructor(private val bookDao: BookDao) : BooksRepo {
    override fun getAllBooksByCategory(category: Category): Single<List<Book>> =
        bookDao.getAll(category)
            .toObservable()
            .map { it.map { it.toDomain() } }
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

interface BooksRepo {
    fun getAllBooksByCategory(category: Category): Single<List<Book>>
}
