package dev.liinahamari.list_ui.single_entity

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import dev.liinahamari.list_ui.R

class SettingsFragment : PreferenceFragmentCompat() {
    companion object {
        @JvmStatic fun newInstance() = SettingsFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) =
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
}
