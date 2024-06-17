package dev.liinahamari.list_ui.single_entity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.viewmodels.CacheClearingViewModel
import dev.liinahamari.list_ui.viewmodels.ClearCacheEvent
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val cacheClearingViewModel: CacheClearingViewModel by viewModels { viewModelFactory }

    companion object {
        @JvmStatic fun newInstance() = SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        super.onViewCreated(view, savedInstanceState)
        //todo ext
        findPreference<Preference>(getString(dev.liinahamari.core.R.string.pref_clear_image_cache))?.setOnPreferenceClickListener {
            cacheClearingViewModel.clearImageCache()
            true
        }
        setupViewModelSubscriptions()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) =
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)

    private fun setupViewModelSubscriptions() {
        cacheClearingViewModel.fetchAllEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ClearCacheEvent.Success -> toast(getString(R.string.image_cache_cleared))
                is ClearCacheEvent.Error -> toast(it.errorMessage)
            }
        }
    }
}
