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
import dev.liinahamari.core.ext.withArguments
import dev.liinahamari.core.views.VerticalSpaceItemDecoration
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.databinding.FragmentCategoryBinding
import dev.liinahamari.list_ui.di.ViewModelBuilderModule
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddBookDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddDocumentaryDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddGameDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddMovieDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddShortDialogFragment
import dev.liinahamari.list_ui.single_entity.add_dialogs.showCountrySelectionDialog
import dev.liinahamari.list_ui.viewmodels.BunchDeleteEvent
import dev.liinahamari.list_ui.viewmodels.CachedCountriesViewModel
import dev.liinahamari.list_ui.viewmodels.DeleteEntryViewModel
import dev.liinahamari.list_ui.viewmodels.FetchAllEvent
import dev.liinahamari.list_ui.viewmodels.FetchViewModel
import dev.liinahamari.list_ui.viewmodels.FetchGamesViewModel
import dev.liinahamari.list_ui.viewmodels.FilterEvent
import dev.liinahamari.list_ui.viewmodels.FindEntityEvent
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
    private val fetchViewModel: FetchViewModel by viewModels { viewModelFactory }
    private val shareEntryViewModel: ShareEntryViewModel by viewModels { viewModelFactory }
    private val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }
    private val deleteEntryViewModel: DeleteEntryViewModel by viewModels { viewModelFactory }
    private val moveToOtherCategoryViewModel: MoveToOtherCategoryViewModel by viewModels { viewModelFactory }
    private val cachedCountriesViewModel: CachedCountriesViewModel by viewModels { viewModelFactory }

    private val argumentEntityCategory: Category by lazy { requireArguments().getParcelableOf(ARG_CATEGORY) }
    private val argumentEntityType: ViewModelBuilderModule.ENTITY_TYPE by lazy { requireArguments().getParcelableOf(ARG_TYPE) }

    private var entriesAdapter: EntryAdapter? = null
//    private val tracker: SelectionTracker<Long> by lazy {
//        ui.entriesRv.createSelectionTracker(
//            ::onSelectionChanged,
//            ::javaClass.name,
//            EntryAdapter.EntryDetailsLookup(ui.entriesRv)
//        )
//    }

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
        fetchViewModel.fetchEntries(argumentEntityCategory)
        setupMenu()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    private fun setupMenu() = requireActivity().addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
            menuInflater.inflate(R.menu.bunch_entities_actions, menu)
            menu.findItem(R.id.filter).setIcon(if (filterEnabled) R.drawable.clear_filter else R.drawable.filter)
            if (argumentEntityType == ViewModelBuilderModule.ENTITY_TYPE.GAME) menu.findItem(R.id.filter).isVisible = false
            this@EntryFragment.menu = menu
        }

        override fun onMenuItemSelected(menuItem: MenuItem) = true.also {
            when (menuItem.itemId) {
                R.id.filter -> if (filterEnabled) {
                    filterEnabled = false
                    fetchViewModel.fetchEntries(argumentEntityCategory)
                    menuItem.setIcon(R.drawable.filter)
                } else {
                    cachedCountriesViewModel.getCachedCountries()
                }

                R.id.delete -> MaterialDialog(requireContext())
                    .message(R.string.sure_to_delete)
                    .negativeButton(android.R.string.cancel)
                    .positiveButton { deleteEntryViewModel.delete(selectedEntitiesIds) }
                    .show()

                R.id.share -> shareEntryViewModel.getById(argumentEntityCategory, selectedEntitiesIds)

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
        Category.entries //todo to usecase
            .toMutableList()
            .apply { removeIf { it == argumentEntityCategory } }
            .forEach { category ->
                add(category.emoji).setOnMenuItemClickListener {
                    moveToOtherCategoryViewModel.moveToOtherCategory(
                        selection = selectedEntitiesIds,
                        actualCategory = argumentEntityCategory,
                        desirableCategory = category
                    )
                    true
                }
            }
    }

    private fun setupViews() {
        setupRecyclerView()
        ui.fab.setOnClickListener { showAppropriateEntityAddFragment() }
        ui.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false
            override fun onQueryTextChange(newText: String) = false.also { /*entriesAdapter!!.filter(newText)*/ }
        })
    }

    private fun setupRecyclerView() {
        ui.entriesRv.addItemDecoration(VerticalSpaceItemDecoration(10))
        entriesAdapter = EntryAdapter(childFragmentManager, ui.entriesRv)
//        ui.entriesRv.adapter = entriesAdapter
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
                        fetchViewModel.filter(argumentEntityCategory, it.first()/*fixme*/)
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
//                    tracker.clearSelection()
                    toast("Successfully deleted")
                    fetchViewModel.fetchEntries(argumentEntityCategory)
                }
            }
        }
        saveEntryViewModel.saveEvent.observe(viewLifecycleOwner) {
            fetchViewModel.fetchEntries(argumentEntityCategory)
        }
        fetchViewModel.fetchAllEvent.observe(viewLifecycleOwner) {
            when (it) {
                is FetchAllEvent.Empty -> {
                    ui.searchAndListLayout.isVisible = false
                    ui.errorView.isVisible = false
                    ui.emptyMessageView.isVisible = true
                    ui.emptyMessageView.text = String.format(
                        Locale.getDefault(),
                        getString(R.string.empty_list_message),
                        argumentEntityType.toString().lowercase().capitalize()//todo mapper
                    )
                }

                is FetchAllEvent.Success<*> -> {
                    println("dasdasadad ${it.entries.first()?.javaClass }}")
                    ui.searchAndListLayout.isVisible = true
                    ui.emptyMessageView.isVisible = false
                    ui.errorView.isVisible = false
//                    entriesAdapter!!.replaceDataset(it.entries)
//                    entriesAdapter!!.tracker = tracker
                }

                is FetchAllEvent.Failure -> {
                    ui.searchAndListLayout.isVisible = false
                    ui.errorView.isVisible = true
                    ui.emptyMessageView.isVisible = false
                }
            }
        }
        fetchViewModel.filterEvent.observe(viewLifecycleOwner) {
            if (it is FilterEvent.Success<*>) {
//                entriesAdapter!!.replaceDataset(it.entries)
//                entriesAdapter!!.tracker = tracker
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
//                    tracker.clearSelection()
                    toast("Successfully changed category")
                    fetchViewModel.fetchEntries(argumentEntityCategory)
                }

                is MoveToOtherCategoryViewModel.SaveEntityEvent.Failure -> toast("Failed to move to other category")
            }
        }
        fetchViewModel.findEntityEvent.observe(viewLifecycleOwner) {
            if (it is FindEntityEvent.Success<*>) {
                showAppropriateEntityAddFragment()
            }
        }
    }

    private fun showAppropriateEntityAddFragment() = when (argumentEntityType) {
        ViewModelBuilderModule.ENTITY_TYPE.BOOK -> AddBookDialogFragment
            .newInstance(argumentEntityCategory)
            .show(childFragmentManager, null)

        ViewModelBuilderModule.ENTITY_TYPE.GAME -> AddGameDialogFragment
            .newInstance(argumentEntityCategory)
            .show(childFragmentManager, null)

        ViewModelBuilderModule.ENTITY_TYPE.MOVIE -> AddMovieDialogFragment
            .newInstance(argumentEntityCategory)
            .show(childFragmentManager, null)

        ViewModelBuilderModule.ENTITY_TYPE.DOCUMENTARY -> AddDocumentaryDialogFragment
            .newInstance(argumentEntityCategory)
            .show(childFragmentManager, null)

        ViewModelBuilderModule.ENTITY_TYPE.SHORT -> AddShortDialogFragment
            .newInstance(argumentEntityCategory)
            .show(childFragmentManager, null)
    }

    companion object {
        const val ARG_TYPE = "arg_type"
        @JvmStatic fun newInstance(entityCategory: Category, entityType: ViewModelBuilderModule.ENTITY_TYPE) =
            EntryFragment().withArguments(ARG_CATEGORY to entityCategory, ARG_TYPE to entityType)
    }
}
