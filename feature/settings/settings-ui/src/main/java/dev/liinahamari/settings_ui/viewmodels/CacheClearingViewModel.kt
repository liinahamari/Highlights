package dev.liinahamari.settings_ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import dev.liinahamari.core.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CacheClearingViewModel @Inject constructor(private val application: Application) : ViewModel() {
    private val _fetchAllEvent = SingleLiveEvent<ClearCacheEvent>()
    val fetchAllEvent: LiveData<ClearCacheEvent> get() = _fetchAllEvent
    fun clearImageCache() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    //todo glide as a dependency and test failure of function
                    Glide.get(application.applicationContext).clearDiskCache() //todo calculate and show cache/image_cache dir size
                }
                _fetchAllEvent.value = ClearCacheEvent.Success
            } catch (e: Exception) {
                _fetchAllEvent.value = ClearCacheEvent.Error(e.message ?: e.stackTraceToString())
            }
        }
    }
}

sealed interface ClearCacheEvent {
    data object Success : ClearCacheEvent
    data class Error(val errorMessage: String) : ClearCacheEvent
}
