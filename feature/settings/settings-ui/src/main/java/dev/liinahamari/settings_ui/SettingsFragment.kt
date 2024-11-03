package dev.liinahamari.settings_ui

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import dev.liinahamari.api.SettingsDeps
import dev.liinahamari.core.ext.toast
import dev.liinahamari.settings.SettingsFactory.getApi
import dev.liinahamari.settings.api.R
import dev.liinahamari.settings.settings_ui.BuildConfig
import dev.liinahamari.settings_ui.di.DaggerSettingsUiComponent
import dev.liinahamari.settings_ui.viewmodels.CacheClearingViewModel
import dev.liinahamari.settings_ui.viewmodels.ClearCacheEvent
import dev.liinahamari.settings_ui.viewmodels.LibraryManagementViewModel
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val cacheClearingViewModel: CacheClearingViewModel by viewModels { viewModelFactory }
    private val libraryManagementViewModel: LibraryManagementViewModel by viewModels { viewModelFactory }

    companion object {
        @JvmStatic fun newInstance() = SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        DaggerSettingsUiComponent.factory().create(getApi(object : SettingsDeps {
            override val application: Application
                get() = this@SettingsFragment.requireActivity().application
        })).inject(this)
        super.onViewCreated(view, savedInstanceState)
        findPreference<Preference>(getString(R.string.pref_clear_image_cache))?.setOnPreferenceClickListener {
            cacheClearingViewModel.clearImageCache()
            true
        }
        findPreference<SwitchPreferenceCompat>(getString(R.string.pref_leakcanary))?.apply {
            if (BuildConfig.DEBUG.not()) {
                findPreference<Preference>(getString(R.string.pref_leakcanary))?.isVisible = false
            } else {
                isChecked = libraryManagementViewModel.getLeakCanaryState()
                setOnPreferenceChangeListener { _, state ->
                    libraryManagementViewModel.enableLeakCanary(state as Boolean)
                    true
                }
            }
        }

        setupViewModelSubscriptions()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) =
        setPreferencesFromResource(dev.liinahamari.settings.settings_ui.R.xml.settings_preferences, rootKey)

    private fun setupViewModelSubscriptions() {
        cacheClearingViewModel.fetchAllEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ClearCacheEvent.Success -> toast(getString(R.string.image_cache_cleared))
                is ClearCacheEvent.Error -> toast(it.errorMessage)
            }
        }
    }
}
