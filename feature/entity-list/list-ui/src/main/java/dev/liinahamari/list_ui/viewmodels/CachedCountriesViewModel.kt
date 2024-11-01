package dev.liinahamari.list_ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.liinahamari.api.domain.entities.Country
import dev.liinahamari.api.domain.usecases.CacheCountriesUseCase
import dev.liinahamari.core.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class CachedCountriesViewModel @Inject constructor(private val cacheCountriesUseCase: CacheCountriesUseCase) :
    ViewModel() {
    private val _fetchCountriesEvent = SingleLiveEvent<GetAllCountriesEvent>()
    val fetchCountriesEvent: LiveData<GetAllCountriesEvent> get() = _fetchCountriesEvent
    fun getCachedCountries() {
        viewModelScope.launch {
            try {
                _fetchCountriesEvent.value = cacheCountriesUseCase.getCachedCountries().let(GetAllCountriesEvent::Success)
            } catch (e: Exception) {
                _fetchCountriesEvent.value = GetAllCountriesEvent.Failure
            }
        }
    }

    sealed interface GetAllCountriesEvent {
        data class Success(val countries: List<Country>) : GetAllCountriesEvent
        data object Failure : GetAllCountriesEvent
    }
}
