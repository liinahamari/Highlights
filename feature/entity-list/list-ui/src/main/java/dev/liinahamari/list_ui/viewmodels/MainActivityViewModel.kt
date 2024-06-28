package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.liinahamari.api.domain.repo.ShakeCounterRepo
import dev.liinahamari.api.domain.usecases.CacheCountriesUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val shakeCounterRepo: ShakeCounterRepo,
    private val cacheCountriesUseCase: CacheCountriesUseCase
) : ViewModel() {
    fun setShaked(isShaked: Boolean) {
        shakeCounterRepo.shaked = isShaked
    }

    fun getShaked() = shakeCounterRepo.shaked

    fun cacheCountries() = viewModelScope.launch { cacheCountriesUseCase.cacheCountries() }
}
