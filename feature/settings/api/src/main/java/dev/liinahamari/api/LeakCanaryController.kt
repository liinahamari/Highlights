package dev.liinahamari.api

interface LeakCanaryController {
    fun enable(state: Boolean)
    fun getState(): Boolean
}
