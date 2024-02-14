package dev.liinahamari.impl

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Maybe.concat(Maybe.just(true).filter { !it }, Maybe.just(true)).toList().subscribe { it -> println("aaa $it") }
    }
}
