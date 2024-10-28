package dev.liinahamari.list_ui.single_entity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionTracker
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.ShareMessage
import dev.liinahamari.core.ext.createSelectionTracker
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.core.views.VerticalSpaceItemDecoration
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
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddShortDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.showCountrySelectionDialog
import dev.liinahamari.list_ui.viewmodels.BunchDeleteEvent
import dev.liinahamari.list_ui.viewmodels.CachedCountriesViewModel
import dev.liinahamari.list_ui.viewmodels.DeleteEntryViewModel
import dev.liinahamari.list_ui.viewmodels.FetchEntriesViewModel
import dev.liinahamari.list_ui.viewmodels.MoveToOtherCategoryViewModel
import dev.liinahamari.list_ui.viewmodels.SaveEntryViewModel
import dev.liinahamari.list_ui.viewmodels.ShareEntryViewModel
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY
import me.saket.cascade.CascadePopupMenu
import java.util.Locale
import javax.inject.Inject

class EntryFragment : Fragment(R.layout.fragment_category) {
    private val ui by viewBinding(FragmentCategoryBinding::bind)
    private lateinit var menu: Menu

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val fetchEntriesViewModel: FetchEntriesViewModel by viewModels { viewModelFactory }
    private val shareEntryViewModel: ShareEntryViewModel by viewModels { viewModelFactory }
    private val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }
    private val deleteEntryViewModel: DeleteEntryViewModel by viewModels { viewModelFactory }
    private val moveToOtherCategoryViewModel: MoveToOtherCategoryViewModel by viewModels { viewModelFactory }
    private val cachedCountriesViewModel: CachedCountriesViewModel by viewModels { viewModelFactory }

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

    private fun shareEntity(shareMessage: ShareMessage) {
        val shareIntent = Intent().apply {
            setAction(Intent.ACTION_SEND)
            setType("text/plain")
            putExtra(Intent.EXTRA_SUBJECT, shareMessage.title)
            putExtra(Intent.EXTRA_TEXT, shareMessage.content)
        }
        startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.share_with)))
    }

    val selectedEntitiesIds = mutableSetOf<Long>()
    private var filterEnabled = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        setupViewModelSubscriptions()
        setupViews()
        fetchEntriesViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
        setupMenu()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    private fun setupMenu() = requireActivity().addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
            menuInflater.inflate(R.menu.bunch_entities_actions, menu)
            menu.findItem(R.id.filter).setIcon(if (filterEnabled) R.drawable.clear_filter else R.drawable.filter)
            if (argumentEntityType == GAME) menu.findItem(R.id.filter).isVisible = false
            this@EntryFragment.menu = menu
        }

        override fun onMenuItemSelected(menuItem: MenuItem) = true.also {
            when (menuItem.itemId) {
                R.id.filter -> if (filterEnabled) {
                    filterEnabled = false
                    fetchEntriesViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
                    menuItem.setIcon(R.drawable.filter)
                } else {
                    cachedCountriesViewModel.getCachedCountries()
                }

                R.id.delete -> MaterialDialog(requireContext())
                    .message(R.string.sure_to_delete)
                    .negativeButton(android.R.string.cancel)
                    .positiveButton { deleteEntryViewModel.delete(selectedEntitiesIds, argumentEntityType) }
                    .show()

                R.id.share -> shareEntryViewModel.getEntitiesById(
                    argumentEntityCategory,
                    argumentEntityType,
                    selectedEntitiesIds
                )

                R.id.moveTo -> with(CascadePopupMenu(requireContext(), requireView())) {
                    menu.apply {
                        MenuCompat.setGroupDividerEnabled(this, true)
                        addSubMenu(getString(R.string.move_to)).setupMoveToEntrySubMenu()
                    }
                    show()
                }
            }
        }
    })

    private fun SubMenu.setupMoveToEntrySubMenu() {
//        setIcon(R.drawable.move) //todo
        Category.values() //todo to usecase
            .toMutableList()
            .apply { removeIf { it == argumentEntityCategory } }
            .forEach { category ->
                add(category.emoji).setOnMenuItemClickListener {
                    moveToOtherCategoryViewModel.moveToOtherCategory(
                        selection = selectedEntitiesIds,
                        actualCategory = argumentEntityCategory,
                        desirableCategory = category,
                        entityType = argumentEntityType
                    )
                    true
                }
            }
    }

    private fun setupViews() {
        setupRecyclerView()
        setupFab()
        ui.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false
            override fun onQueryTextChange(newText: String) = false.also { entriesAdapter!!.filter(newText) }
        })
    }

    private fun setupRecyclerView() {
        ui.entriesRv.addItemDecoration(VerticalSpaceItemDecoration(10))
        entriesAdapter = EntryAdapter(childFragmentManager, ui.entriesRv)
        ui.entriesRv.adapter = entriesAdapter
    }

    private fun onSelectionChanged(selection: Selection<Long>) {
        with(selection.isEmpty.not()) {
            menu.findItem(R.id.delete).isVisible = this
            menu.findItem(R.id.moveTo).isVisible = this
        }
        selectedEntitiesIds.clear()
        selectedEntitiesIds.addAll(selection)
    }

    private fun setupViewModelSubscriptions() {
        cachedCountriesViewModel.fetchCountriesEvent.observe(viewLifecycleOwner) {
            when (it) {
                is CachedCountriesViewModel.GetAllCountriesEvent.Failure -> toast("Failed to get countries' list")
                is CachedCountriesViewModel.GetAllCountriesEvent.Success -> {
                    showCountrySelectionDialog(countries = it.countries) {
                        fetchEntriesViewModel.filter(argumentEntityType, argumentEntityCategory, it.first()/*fixme*/)
                        filterEnabled = true
                        menu.getItem(R.id.filter).setIcon(R.drawable.clear_filter)
                    }
                }
            }
        }
        deleteEntryViewModel.bunchDeleteEvent.observe(viewLifecycleOwner) {
            when (it) {
                is BunchDeleteEvent.Failure -> toast("Failed to delete")
                is BunchDeleteEvent.Success -> {
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
            when (it) {
                is FetchEntriesViewModel.FetchAllEvent.Empty -> {
                    ui.searchAndListLayout.isVisible = false
                    ui.errorView.isVisible = false
                    ui.emptyMessageView.isVisible = true
                    ui.emptyMessageView.text = String.format(
                        Locale.getDefault(),
                        getString(R.string.empty_list_message),
                        argumentEntityType.toString().lowercase().capitalize()//todo mapper
                    )
                }

                is FetchEntriesViewModel.FetchAllEvent.Success -> {
                    ui.searchAndListLayout.isVisible = true
                    ui.emptyMessageView.isVisible = false
                    ui.errorView.isVisible = false
                    entriesAdapter!!.replaceDataset(it.entries)
                    entriesAdapter!!.tracker = tracker
                }

                is FetchEntriesViewModel.FetchAllEvent.Failure -> {
                    ui.searchAndListLayout.isVisible = false
                    ui.errorView.isVisible = true
                    ui.emptyMessageView.isVisible = false
                }
            }
        }
        fetchEntriesViewModel.filterEvent.observe(viewLifecycleOwner) {
            if (it is FetchEntriesViewModel.FilterEvent.Success) {
                entriesAdapter!!.replaceDataset(it.entries)
                entriesAdapter!!.tracker = tracker
            }
        }
        shareEntryViewModel.shareEntryEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ShareEntryViewModel.ShareEntry.Success -> shareEntity(it.data)
                is ShareEntryViewModel.ShareEntry.Failure -> toast("Failed to get info about entities to share")
            }
        }
        moveToOtherCategoryViewModel.saveEntityEvent.observe(viewLifecycleOwner) {
            when (it) {
                is MoveToOtherCategoryViewModel.SaveEntityEvent.Success -> {
                    tracker.clearSelection()
                    toast("Successfully changed category")
                    fetchEntriesViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
                }

                is MoveToOtherCategoryViewModel.SaveEntityEvent.Failure -> toast("Failed to move to other category")
            }
        }
        fetchEntriesViewModel.findEntityEvent.observe(viewLifecycleOwner) {
            if (it is FetchEntriesViewModel.FindEntityEvent.Success) {
                showAppropriateEntityAddFragment()
            }
        }
    }

    private fun setupFab() {
        ui.fab.setOnClickListener { showAppropriateEntityAddFragment() }
    }

    private fun showAppropriateEntityAddFragment() = when (argumentEntityType) {
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

        EntityType.SHORT -> AddShortDialogFragment
            .newInstance(argumentEntityCategory)
            .show(childFragmentManager, null)
    }

    companion object {
        const val ARG_TYPE = "arg_type"
        @JvmStatic fun newInstance(entityCategory: Category, entityType: EntityType) =
            EntryFragment().apply {
                arguments = bundleOf(ARG_CATEGORY to entityCategory, ARG_TYPE to entityType)
            }
    }
}
