package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.app.Dialog
import android.app.SearchManager.QUERY
import android.content.Context
import android.content.DialogInterface.BUTTON_NEUTRAL
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import dev.liinahamari.api.PreferencesRepo
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.viewmodels.SaveEntryViewModel
import dev.liinahamari.list_ui.viewmodels.SaveEvent
import javax.inject.Inject

abstract class AddFragment(@LayoutRes val layout: Int) : DialogFragment(layout) {
    @Inject lateinit var preferenceRepo: PreferencesRepo
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    protected val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        super.onAttach(context)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupViewModelSubscriptions()
    }

    private fun setupUi() {
        setupTextChangedListeners()
        setupSelectionDialogs()
        setupTitleEditText()
    }

    abstract fun setupTitleEditText()
    abstract fun setupSelectionDialogs()
    abstract fun setupTextChangedListeners()

    @CallSuper
    protected open fun setupViewModelSubscriptions() {
        saveEntryViewModel.saveEvent.observe(this) {
            when (it) {
                is SaveEvent.Failure -> toast("Failed to save book")
                is SaveEvent.Success -> requireActivity().supportFragmentManager.popBackStackImmediate()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
        .setView(getDialogCustomView())
        .setNeutralButton(R.string.internet_search) { _, _ -> }
        .setPositiveButton(R.string.save, onSaveButtonClicked())
        .create()
        .apply {
            setOnShowListener {
                (dialog as AlertDialog)
                    .getButton(BUTTON_NEUTRAL)
                    .setOnClickListener {
                        startActivity(Intent(Intent.ACTION_WEB_SEARCH).putExtra(QUERY, webSearchQuery()))
                    }
            }
        }

    abstract fun getDialogCustomView(): LinearLayout
    abstract fun onSaveButtonClicked(): OnClickListener
    abstract fun webSearchQuery(): String
}
