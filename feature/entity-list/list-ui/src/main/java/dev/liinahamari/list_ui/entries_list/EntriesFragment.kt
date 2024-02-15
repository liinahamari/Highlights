@file:Suppress("DEPRECATION")

package dev.liinahamari.list_ui.entries_list

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.os.BuildCompat.isAtLeastT
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.liinahamari.api.DATABASE_NAME
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.providePermissionExplanationDialog
import dev.liinahamari.core.ext.restartApp
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.MainActivity
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentEntriesBinding
import dev.liinahamari.list_ui.single_entity.EntityType
import dev.liinahamari.list_ui.single_entity.EntryFragment
import java.lang.IllegalStateException
import javax.inject.Inject

private const val PROCESS_BACKUP = 1
private const val PROCESS_RESTORE = 2

class EntriesFragment : Fragment(R.layout.fragment_entries) {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val entriesViewModel: EntriesViewModel by viewModels { viewModelFactory }

    private val ui: FragmentEntriesBinding by viewBinding(FragmentEntriesBinding::bind)

    private var currentProcess: Int? = null

    private val permissionRequestLauncher = registerForActivityResult(RequestMultiplePermissions()) { permissions ->
        if (permissions.values.all { it }.not()) {
            providePermissionExplanationDialog(
                getString(R.string.title_app_needs_permission),
                getString(R.string.backup_permission_explanation)
            ).show()
        } else {
            when (currentProcess) {
                PROCESS_BACKUP -> backupFileCreator.launch(DATABASE_NAME)
                PROCESS_RESTORE -> openBackupFileChooser.launch(arrayOf("application/octet-stream"))
                else -> throw IllegalStateException()
            }
        }
    }

    private val openBackupFileChooser = registerForActivityResult(OpenDocument()) { result ->
        entriesViewModel.restoreDatabase(result)
    }

    private val backupFileCreator = registerForActivityResult(CreateDocument("application/octet-stream")) { result ->
        entriesViewModel.saveDatabase(result)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        setupViewModelSubscriptions()
        setupClicks()
    }

    private fun setupClicks() {
        ui.entityButtonRv.adapter = EntityButtonsAdapter(::onEntityClicked)
        ui.backupFab.setOnClickListener {
            currentProcess = PROCESS_BACKUP
            permissionRequestLauncher.launch(
                if (isAtLeastT()) arrayOf(
                    READ_MEDIA_IMAGES,
                    READ_MEDIA_AUDIO,
                    READ_MEDIA_VIDEO
                ) else arrayOf(READ_EXTERNAL_STORAGE)
            )
        }

        ui.restoreFab.setOnClickListener {
            currentProcess = PROCESS_RESTORE
            permissionRequestLauncher.launch(
                if (isAtLeastT()) arrayOf(
                    READ_MEDIA_IMAGES,
                    READ_MEDIA_AUDIO,
                    READ_MEDIA_VIDEO
                ) else arrayOf(READ_EXTERNAL_STORAGE)
            )
        }
    }

    private fun setupViewModelSubscriptions() {
        entriesViewModel.successfulBackupEvent.observe(viewLifecycleOwner, ::toast)
        entriesViewModel.errorEvent.observe(viewLifecycleOwner, ::toast)
        entriesViewModel.restoreDatabase.observe(viewLifecycleOwner) {
            toast("Import Completed")
            restartApp(MainActivity::class.java)
        }
    }

    private fun onEntityClicked(entityType: EntityType) = parentFragmentManager.beginTransaction()
        .add(
            R.id.pagerContainer,
            EntryFragment.newInstance(requireArguments().getParcelableOf(ARG_CATEGORY), entityType)
        )
        .addToBackStack(null)
        .commit()

    companion object {
        const val ARG_CATEGORY = "arg_category"
        @JvmStatic fun newInstance(category: Category) =
            EntriesFragment().apply { arguments = bundleOf(ARG_CATEGORY to category) }
    }
}
