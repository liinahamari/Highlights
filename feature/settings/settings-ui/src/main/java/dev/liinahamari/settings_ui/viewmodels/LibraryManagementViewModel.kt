package dev.liinahamari.settings_ui.viewmodels

import androidx.lifecycle.ViewModel
import dev.liinahamari.api.LeakCanaryController
import javax.inject.Inject

class LibraryManagementViewModel @Inject constructor(private val lcController: LeakCanaryController) : ViewModel() {
    fun enableLeakCanary(state: Boolean) = lcController.enable(state)
    fun getLeakCanaryState(): Boolean = lcController.getState()
}
