package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.ShakeCounterRepo
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val shakeCounterRepo: ShakeCounterRepo
) : ViewModel() {
    fun setShaked(isShaked: Boolean) {
        shakeCounterRepo.shaked = isShaked
    }
    fun getShaked() = shakeCounterRepo.shaked
}
