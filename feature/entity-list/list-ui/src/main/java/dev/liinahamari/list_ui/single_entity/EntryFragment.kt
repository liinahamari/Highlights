package dev.liinahamari.list_ui.single_entity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionTracker
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.createSelectionTracker
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.databinding.FragmentCategoryBinding
import dev.liinahamari.list_ui.single_entity.EntityType.BOOK
import dev.liinahamari.list_ui.single_entity.EntityType.DOCUMENTARY
import dev.liinahamari.list_ui.single_entity.EntityType.GAME
import dev.liinahamari.list_ui.single_entity.EntityType.MOVIE
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddBookDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddDocumentaryDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddGameDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddMovieDialogFragment
import dev.liinahamari.list_ui.viewmodels.BunchDeleteEvent
import dev.liinahamari.list_ui.viewmodels.DeleteEntryViewModel
import dev.liinahamari.list_ui.viewmodels.FetchEntriesViewModel
import dev.liinahamari.list_ui.viewmodels.MoveToOtherCategoryViewModel
import dev.liinahamari.list_ui.viewmodels.SaveEntryViewModel
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY
import javax.inject.Inject

class EntryFragment : Fragment(R.layout.fragment_category) {
    private val ui by viewBinding(FragmentCategoryBinding::bind)
    private lateinit var menu: Menu

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val fetchEntriesViewModel: FetchEntriesViewModel by viewModels { viewModelFactory }
    private val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }
    private val deleteEntryViewModel: DeleteEntryViewModel by viewModels { viewModelFactory }
    private val moveToOtherCategoryViewModel: MoveToOtherCategoryViewModel by viewModels { viewModelFactory }

    private val argumentEntityType: EntityType by lazy { requireArguments().getParcelableOf(ARG_TYPE) }
    private val argumentEntityCategory: Category by lazy { requireArguments().getParcelableOf(ARG_CATEGORY) }

    private var entriesAdapter: EntryAdapter? = null
    private val tracker: SelectionTracker<Long> by lazy {
        ui.entriesRv.createSelectionTracker(
            ::onSelectionChanged,
            ::javaClass.name,
            EntryAdapter.EntryDetailsLookup(ui.entriesRv)
        )
    }

    val entitiesIdToDelete = mutableSetOf<Long>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        setupViewModelSubscriptions()
        setupViews()
        fetchEntriesViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
        setupMenu()
    }

    private fun setupMenu() = requireActivity().addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.bunch_entities_actions, menu)
            this@EntryFragment.menu = menu
        }

        override fun onMenuItemSelected(menuItem: MenuItem) = true.also {
            if (menuItem.itemId == R.id.delete) {
                MaterialDialog(requireContext())
                    .message(R.string.sure_to_delete)
                    .negativeButton(android.R.string.cancel)
                    .positiveButton { deleteEntryViewModel.delete(entitiesIdToDelete, argumentEntityType) }
                    .show()
            }
        }
    })

    private fun setupViews() {
        entriesAdapter = EntryAdapter(childFragmentManager, ui.entriesRv)
        ui.entriesRv.adapter = entriesAdapter
        setupFab()
        ui.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false
            override fun onQueryTextChange(newText: String) = false.also { entriesAdapter!!.filter(newText) }
        })
    }

    private fun onSelectionChanged(selection: Selection<Long>) {
        if (selection.isEmpty.not()) {
            (requireActivity() as AppCompatActivity).supportActionBar?.show()
        }
        menu.findItem(R.id.delete)!!.setVisible(selection.isEmpty.not())
        entitiesIdToDelete.clear()
        entitiesIdToDelete.addAll(selection)
    }

    private fun setupViewModelSubscriptions() {
        deleteEntryViewModel.bunchDeleteEvent.observe(viewLifecycleOwner) {
            when (it) {
                is BunchDeleteEvent.Failure -> toast("Failed to delete")
                is BunchDeleteEvent.Success -> {
                    (requireActivity() as AppCompatActivity).supportActionBar?.hide()
                    tracker.clearSelection()
                    toast("Successfully deleted")
                    fetchEntriesViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
                }
            }
        }
        saveEntryViewModel.saveEvent.observe(viewLifecycleOwner) {
            fetchEntriesViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
        }
        fetchEntriesViewModel.fetchAllEvent.observe(viewLifecycleOwner) {
            if (it is FetchEntriesViewModel.FetchAllEvent.Success) {
                entriesAdapter!!.replaceDataset(it.entries)
                entriesAdapter!!.tracker = tracker
            }
        }
        moveToOtherCategoryViewModel.saveEntityEvent.observe(viewLifecycleOwner) {
            when (it) {
                is MoveToOtherCategoryViewModel.SaveEntityEvent.Success -> entriesAdapter!!.removeItem(it.adapterPosition)
                is MoveToOtherCategoryViewModel.SaveEntityEvent.Failure -> toast("Failed to move to other category")
            }
        }
        fetchEntriesViewModel.findEntityEvent.observe(viewLifecycleOwner) {
            if (it is FetchEntriesViewModel.FindEntityEvent.Success) {
                when (argumentEntityType) {
                    BOOK -> AddBookDialogFragment
                        .newInstance(argumentEntityCategory)
                        .show(childFragmentManager, null)

                    GAME -> AddGameDialogFragment
                        .newInstance(argumentEntityCategory)
                        .show(childFragmentManager, null)

                    MOVIE -> AddMovieDialogFragment
                        .newInstance(argumentEntityCategory)
                        .show(childFragmentManager, null)

                    DOCUMENTARY -> AddDocumentaryDialogFragment
                        .newInstance(argumentEntityCategory)
                        .show(childFragmentManager, null)
                }
            }
        }
    }

    private fun setupFab() {
        ui.fab.setOnClickListener {
            when (argumentEntityType) {
                BOOK -> AddBookDialogFragment
                    .newInstance(argumentEntityCategory)
                    .show(childFragmentManager, null)

                GAME -> AddGameDialogFragment
                    .newInstance(argumentEntityCategory)
                    .show(childFragmentManager, null)

                MOVIE -> AddMovieDialogFragment
                    .newInstance(argumentEntityCategory)
                    .show(childFragmentManager, null) //todo check if dialog showing

                DOCUMENTARY -> AddDocumentaryDialogFragment
                    .newInstance(argumentEntityCategory)
                    .show(childFragmentManager, null)
            }
        }
    }

    companion object {
        const val ARG_TYPE = "arg_type"
        @JvmStatic fun newInstance(entityCategory: Category, entityType: EntityType) =
            EntryFragment().apply {
                arguments = bundleOf(ARG_CATEGORY to entityCategory, ARG_TYPE to entityType)
            }
    }
}
