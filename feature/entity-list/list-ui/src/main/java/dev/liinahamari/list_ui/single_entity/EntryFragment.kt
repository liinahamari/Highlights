package dev.liinahamari.list_ui.single_entity

import android.os.Bundle
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentCategoryBinding
import dev.liinahamari.list_ui.single_entity.EntityType.BOOK
import dev.liinahamari.list_ui.single_entity.EntityType.DOCUMENTARY
import dev.liinahamari.list_ui.single_entity.EntityType.GAME
import dev.liinahamari.list_ui.single_entity.EntityType.MOVIE
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddBookDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddDocumentaryDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddGameDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddMovieDialogFragment
import dev.liinahamari.list_ui.viewmodels.DeleteEntryViewModel
import dev.liinahamari.list_ui.viewmodels.DeleteEvent
import dev.liinahamari.list_ui.viewmodels.FetchEntriesViewModel
import dev.liinahamari.list_ui.viewmodels.MoveToOtherCategoryViewModel
import dev.liinahamari.list_ui.viewmodels.SaveEntryViewModel
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY
import me.saket.cascade.CascadePopupMenu
import javax.inject.Inject

class EntryFragment : Fragment(R.layout.fragment_category), LongClickListener {
    private val ui by viewBinding(FragmentCategoryBinding::bind)

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val fetchEntriesViewModel: FetchEntriesViewModel by viewModels { viewModelFactory }
    private val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }
    private val deleteEntryViewModel: DeleteEntryViewModel by viewModels { viewModelFactory }
    private val moveToOtherCategoryViewModel: MoveToOtherCategoryViewModel by viewModels { viewModelFactory }

    private val argumentEntityType: EntityType by lazy { requireArguments().getParcelableOf(ARG_TYPE) }
    private val argumentEntityCategory: Category by lazy { requireArguments().getParcelableOf(ARG_CATEGORY) }

    private var entriesAdapter: EntryAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        setupViewModelSubscriptions()
        setupViews()

        fetchEntriesViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
    }

    private fun setupViews() {
        entriesAdapter = EntryAdapter(this, childFragmentManager, ui.entriesRv)
        ui.entriesRv.adapter = entriesAdapter
        setupFab()
        ui.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false
            override fun onQueryTextChange(newText: String) = false.also { entriesAdapter!!.filter(newText) }
        })
    }

    private fun setupViewModelSubscriptions() {
        deleteEntryViewModel.deleteEvent.observe(viewLifecycleOwner) {
            when (it) {
                is DeleteEvent.Failure -> Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                is DeleteEvent.Success -> {
                    toast("Successfully deleted")
                    entriesAdapter!!.removeItem(it.position)
                }
            }
        }
        saveEntryViewModel.saveEvent.observe(viewLifecycleOwner) {
            fetchEntriesViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
        }
        fetchEntriesViewModel.fetchAllEvent.observe(viewLifecycleOwner) {
            if (it is FetchEntriesViewModel.FetchAllEvent.Success) {
                entriesAdapter!!.replaceDataset(it.entries)
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

    override fun onLongClicked(id: Long, clazz: Class<*>, position: Int) =
        with(CascadePopupMenu(requireContext(), requireView())) {
            menu.apply {
                MenuCompat.setGroupDividerEnabled(this, true)

                addSubMenu(getString(R.string.move_to)).setupMoveToEntrySubMenu(id, position)
                add(getString(R.string.title_edit)).setupEditEntry(id)
                add(getString(R.string.delete)).setupDeleteEntry(id, position, clazz)
            }
            show()
        }

    private fun SubMenu.setupMoveToEntrySubMenu(id: Long, adapterPosition: Int) {
//        setIcon(R.drawable.move) //todo
        Category.values() //todo to usecase
            .toMutableList()
            .apply { removeIf { it == argumentEntityCategory } }
            .forEach { category ->
                add(category.emoji).setOnMenuItemClickListener {
                    moveToOtherCategoryViewModel.moveToOtherCategory(
                        adapterPosition = adapterPosition,
                        id = id,
                        actualCategory = argumentEntityCategory,
                        desirableCategory = category,
                        entityType = argumentEntityType
                    )
                    true
                }
            }
    }

    private fun MenuItem.setupEditEntry(id: Long) {
        setOnMenuItemClickListener {
            fetchEntriesViewModel.findEntry(argumentEntityType, argumentEntityCategory, id)
            true
        }
        setIcon(R.drawable.baseline_edit_24)
    }

    private fun MenuItem.setupDeleteEntry(id: Long, position: Int, clazz: Class<*>) {
        setOnMenuItemClickListener {
            MaterialDialog(requireContext())
                .message(R.string.sure_to_delete)
                .negativeButton(android.R.string.cancel)
                .positiveButton { deleteEntryViewModel.deleteEntity(id, position, clazz) }
                .show()
            true
        }
        setIcon(R.drawable.baseline_delete_24)
    }
}
