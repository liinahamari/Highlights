package dev.liinahamari.list_ui.entries_list

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.BuildCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.providePermissionExplanationDialog
import dev.liinahamari.list_ui.MainActivity
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentEntriesBinding
import dev.liinahamari.list_ui.single_entity.EntityType
import dev.liinahamari.list_ui.single_entity.EntryFragment
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

private const val PROCESS_BACKUP = 1
private const val PROCESS_RESTORE = 2

class EntriesFragment : Fragment(R.layout.fragment_entries) {
    private var currentProcess: Int? = null

    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.all { it }.not()) {
                providePermissionExplanationDialog(
                    getString(R.string.title_app_needs_permission),
                    getString(R.string.backup_permission_explanation)
                ).show()
            } else {
                when (currentProcess) {
                    PROCESS_BACKUP -> openBackupFileCreator.launch(entriesViewModel.getDatabaseName())
                    PROCESS_RESTORE -> openBackupFileChooser.launch(arrayOf("application/octet-stream"))
                }
            }
        }

    private val openBackupFileChooser = registerForActivityResult(ActivityResultContracts.OpenDocument()) { result ->
        if (result != null) {
            requireContext().contentResolver.openInputStream(result)?.let(entriesViewModel::restore)
        } else {
            providePermissionExplanationDialog(
                getString(R.string.title_app_needs_permission),
                getString(R.string.restore_permission_explanation)
            ).show()
        }
    }

    private val openBackupFileCreator =
        registerForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { result ->
            if (result != null) {
                requireContext().contentResolver.openOutputStream(result)?.let(entriesViewModel::backup)
            } else {
                providePermissionExplanationDialog(
                    getString(R.string.title_app_needs_permission),
                    getString(R.string.backup_permission_explanation)
                ).show()
            }
        }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val entriesViewModel: EntriesViewModel by viewModels { viewModelFactory }

    private val ui: FragmentEntriesBinding by viewBinding(FragmentEntriesBinding::bind)

    companion object {
        const val ARG_CATEGORY = "arg_category"
        @JvmStatic fun newInstance(category: Category) =
            EntriesFragment().apply { arguments = bundleOf(ARG_CATEGORY to category) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        entriesViewModel.setOnBackupSucceedCallback {
            startActivity(Intent(requireActivity(), MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            requireActivity().finish()
            Runtime.getRuntime().exit(0)
        }

        ui.entityButtonRv.adapter = EntityButtonsAdapter(::onEntityClicked)
        ui.backupFab.setOnClickListener {
            currentProcess = PROCESS_BACKUP
            entriesViewModel.init()
            permissionRequestLauncher.launch(
                if (BuildCompat.isAtLeastT()) arrayOf(
                    READ_MEDIA_IMAGES,
                    READ_MEDIA_AUDIO,
                    READ_MEDIA_VIDEO
                ) else arrayOf(READ_EXTERNAL_STORAGE)
            )
        }

        ui.restoreFab.setOnClickListener {
            currentProcess = PROCESS_RESTORE
            permissionRequestLauncher.launch(
                if (BuildCompat.isAtLeastT()) arrayOf(
                    READ_MEDIA_IMAGES,
                    READ_MEDIA_AUDIO,
                    READ_MEDIA_VIDEO
                ) else arrayOf(READ_EXTERNAL_STORAGE)
            )
        }
    }

    private fun onEntityClicked(entityType: EntityType) {
        parentFragmentManager.beginTransaction()
            .add(
                R.id.pagerContainer,
                EntryFragment.newInstance(requireArguments().getParcelableOf(ARG_CATEGORY), entityType)
            )
            .addToBackStack("123")
            .commit()
    }
}

class EntriesViewModel @Inject constructor(
//    private val backupTool: RoomBackupTool
) : ViewModel() {
    fun backup(outputStream: OutputStream) {} /*= backupTool.doBackup(outputStream)*/
    fun restore(inputStream: InputStream) {} /*backupTool.doRestore(inputStream)*/

    fun init() {
//        backupTool.initRoomBackup()
    }

    fun setOnBackupSucceedCallback(doOnBackupSucceed: () -> Unit) {
        /*backupTool.onCompleteListener = object : OnCompleteListener {
            override fun onComplete(success: Boolean, exitCode: Int) {
                if (success) doOnBackupSucceed.invoke()
            }
        }*/
    }

    fun getDatabaseName(): String = "backupTool.getDatabaseName()"
}
