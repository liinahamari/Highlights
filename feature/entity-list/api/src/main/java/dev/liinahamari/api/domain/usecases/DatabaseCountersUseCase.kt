package dev.liinahamari.api.domain.usecases

import io.reactivex.rxjava3.core.Single

interface DatabaseCountersUseCase {
    fun getTotalAmount(): Single<Int>
    fun getMoviesAmount(): Single<Int>
    fun getDocumentariesAmount(): Single<Int>
    fun getBooksAmount(): Single<Int>
    fun getGamesAmount(): Single<Int>
}
