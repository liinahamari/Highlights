package com.example.impl

import dev.liinahamari.impl.domain.RestoreDatabaseUseCaseImpl
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
        RestoreDatabaseUseCaseImpl(mock(), mock())
        assertEquals(4, 2 + 2)
    }
}
