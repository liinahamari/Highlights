package dev.liinahamari.impl.data.repos

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.MainThread
import dev.liinahamari.api.PreferencesRepo
import dev.liinahamari.impl.di.modules.APP_CONTEXT
import dev.liinahamari.settings.api.R
import javax.inject.Inject
import javax.inject.Named

class PreferencesRepoImpl @Inject constructor(
    private val preferences: SharedPreferences,
    @Named(APP_CONTEXT) private val ctx: Context
) : PreferencesRepo {
    @get:MainThread
    override var suggestionsEnabled: Boolean
        get() = preferences.getBoolean(ctx.getString(R.string.pref_enable_suggestions), true)
        set(value) { preferences.edit().putBoolean(ctx.getString(R.string.pref_enable_suggestions), value).apply() }
}
