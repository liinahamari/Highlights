package dev.liinahamari.list_ui.entries_list

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.core.os.BuildCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.usecases.CloseDbEvent
import dev.liinahamari.api.domain.usecases.CloseDbUseCase
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.core.ext.contentResolver
import dev.liinahamari.core.ext.getDatabasePath
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.providePermissionExplanationDialog
import dev.liinahamari.core.ext.restartApp
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.MainActivity
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentEntriesBinding
import dev.liinahamari.list_ui.single_entity.EntityType
import dev.liinahamari.list_ui.single_entity.EntryFragment
import java.io.File
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
                    PROCESS_BACKUP -> entriesViewModel.closeDb()
                    PROCESS_RESTORE -> openBackupFileChooser.launch(arrayOf("application/octet-stream"))
                }
            }
        }

    private val openBackupFileChooser = registerForActivityResult(OpenDocument()) { result ->
        if (result != null) {
            entriesViewModel.closeDb()
            File("/data/data/dev.liinahamari.highlights/", "databases").mkdir()
            contentResolver.openInputStream(result)!!
                .use { input -> getDatabasePath("entries-db").outputStream().use(input::copyTo) }
        } else {
            providePermissionExplanationDialog(
                getString(R.string.title_app_needs_permission),
                getString(R.string.restore_permission_explanation)
            ).show()
        }
    }

    private val backupFileCreator =
        registerForActivityResult(CreateDocument("application/octet-stream")) { result ->
            if (result != null) {
                File(getDatabasePath("entries-db").absolutePath)
                    .inputStream()
                    .buffered()
                    .use { contentResolver.openOutputStream(result)!!.use(it::copyTo) }
                toast("Backup successful")
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

        setupViewModelSubscriptions()

        ui.entityButtonRv.adapter = EntityButtonsAdapter(::onEntityClicked)
        ui.backupFab.setOnClickListener {
            currentProcess = PROCESS_BACKUP
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

    private fun setupViewModelSubscriptions() {
        entriesViewModel.closeDbEvent.observe(viewLifecycleOwner) {
            when (it) {
                CloseDbEvent.Success -> when (currentProcess) {
                    PROCESS_BACKUP -> backupFileCreator.launch("entries-db")
                    PROCESS_RESTORE -> {
                        toast("Import Completed")
                        restartApp(MainActivity::class.java)
                    }
                }

                CloseDbEvent.Error -> toast("Database closing failed")
            }
        }
    }

    private fun onEntityClicked(entityType: EntityType) {
        parentFragmentManager.beginTransaction()
            .add(
                R.id.pagerContainer,
                EntryFragment.newInstance(requireArguments().getParcelableOf(ARG_CATEGORY), entityType)
            )
            .addToBackStack(null)
            .commit()
    }
}

class EntriesViewModel @Inject constructor(
    private val closeDbUseCase: CloseDbUseCase
) : ViewModel() {
    private val _closeDbEvent = SingleLiveEvent<CloseDbEvent>()
    val closeDbEvent: LiveData<CloseDbEvent> get() = _closeDbEvent
    fun closeDb() = closeDbUseCase.closeDb().subscribe { result -> _closeDbEvent.postValue(result) }
}
