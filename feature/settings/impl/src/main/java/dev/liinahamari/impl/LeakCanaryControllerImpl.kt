package dev.liinahamari.impl

import dev.liinahamari.api.LeakCanaryController
import leakcanary.LeakCanary
import javax.inject.Inject

class LeakCanaryControllerImpl @Inject constructor(): LeakCanaryController {
    override fun enable(state: Boolean) {
        LeakCanary.config = LeakCanary.config.copy(state)
        LeakCanary.showLeakDisplayActivityLauncherIcon(state)
    }

    override fun getState(): Boolean = LeakCanary.config.dumpHeap
}
