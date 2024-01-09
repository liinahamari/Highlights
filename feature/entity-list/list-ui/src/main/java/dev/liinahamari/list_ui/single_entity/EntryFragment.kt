package dev.liinahamari.list_ui.single_entity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.list_ui.MainActivity
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentCategoryBinding
import dev.liinahamari.list_ui.single_entity.EntityType.BOOK
import dev.liinahamari.list_ui.single_entity.EntityType.DOCUMENTARY
import dev.liinahamari.list_ui.single_entity.EntityType.GAME
import dev.liinahamari.list_ui.single_entity.EntityType.MOVIE
import dev.liinahamari.list_ui.single_entity.add_dialogs.AddMovieDialogFragment
import dev.liinahamari.list_ui.viewmodels.DeleteEntryViewModel
import dev.liinahamari.list_ui.viewmodels.DeleteEvent
import dev.liinahamari.list_ui.viewmodels.FetchEntriesViewModel
import dev.liinahamari.list_ui.viewmodels.SaveEntryViewModel
import dev.liinahamari.suggestions_ui.ARG_CATEGORY
import me.saket.cascade.CascadePopupMenu
import javax.inject.Inject

class EntryFragment : Fragment(R.layout.fragment_category), LongClickListener {
    private val ui by viewBinding(FragmentCategoryBinding::bind)

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val fetchEntriesViewModel: FetchEntriesViewModel by viewModels { viewModelFactory }
    private val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }
    private val deleteEntryViewModel: DeleteEntryViewModel by viewModels { viewModelFactory }

    private val argumentEntityType: EntityType by lazy { requireArguments().getParcelableOf(ARG_TYPE) }
    private val argumentEntityCategory: Category by lazy { requireArguments().getParcelableOf(ARG_CATEGORY) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        setupViewModelSubscriptions()
        setupFab()
        fetchEntriesViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
    }

    private fun setupViewModelSubscriptions() {
        deleteEntryViewModel.deleteEvent.observe(viewLifecycleOwner) {
            when (it) {
                is DeleteEvent.Failure -> Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                is DeleteEvent.Success -> {
                    Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show()
                    (ui.entriesRv.adapter as EntryAdapter).dataSet.removeAt(it.position)
                    ui.entriesRv.adapter?.notifyItemRemoved(it.position)
                }
            }
        }
        saveEntryViewModel.saveEvent.observe(viewLifecycleOwner) {
            fetchEntriesViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
        }
        fetchEntriesViewModel.fetchAllEvent.observe(viewLifecycleOwner) {
            if (it is FetchEntriesViewModel.FetchAllEvent.Success) {
                ui.entriesRv.adapter = EntryAdapter(it.entries.toMutableList(), this)
                    .apply { notifyDataSetChanged() }
            }
        }
        fetchEntriesViewModel.findEntityEvent.observe(viewLifecycleOwner) {
            if (it is FetchEntriesViewModel.FindEntityEvent.Success) {
                when (argumentEntityType) {
                    BOOK -> showAddBookDialog(
                        argumentEntityCategory,
                        saveEntryViewModel::saveBook,
                        book = it.entry as Book
                    )

                    GAME -> showAddGameDialog(
                        argumentEntityCategory,
                        saveEntryViewModel::saveGame,
                        game = it.entry as Game
                    )

                    MOVIE -> AddMovieDialogFragment
                        .newInstance(argumentEntityCategory)
                        .show(childFragmentManager, null)

                    DOCUMENTARY -> showAddDocumentaryDialog(
                        argumentEntityCategory,
                        saveEntryViewModel::saveDocumentary,
                        documentary = it.entry as Documentary
                    )
                }
            }
        }
    }

    private fun setupFab() {
        ui.fab.setOnClickListener {
            when (argumentEntityType) {
                BOOK -> showAddBookDialog(argumentEntityCategory, saveEntryViewModel::saveBook)
                GAME -> showAddGameDialog(argumentEntityCategory, saveEntryViewModel::saveGame)
                MOVIE -> AddMovieDialogFragment
                    .newInstance(argumentEntityCategory)
                    .show(childFragmentManager, null) //todo check if dialog showing

                DOCUMENTARY -> showAddDocumentaryDialog(argumentEntityCategory, saveEntryViewModel::saveDocumentary)
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

    override fun onLongClicked(id: Long, clazz: Class<*>, position: Int) {
        with(CascadePopupMenu(requireContext(), requireView())) {
            menu.apply {
                MenuCompat.setGroupDividerEnabled(this, true)

                add(getString(R.string.title_edit)).also {
                    it.setOnMenuItemClickListener {
                        fetchEntriesViewModel.findEntry(argumentEntityType, argumentEntityCategory, id)
                        true
                    }
                    it.setIcon(R.drawable.baseline_edit_24)
                }
                add(getString(R.string.delete)).also {
                    it.setOnMenuItemClickListener {
                        MaterialDialog(requireContext())
                            .message(R.string.sure_to_delete)
                            .negativeButton(android.R.string.cancel)
                            .positiveButton {
                                when (clazz) {
                                    Book::class.java -> deleteEntryViewModel.deleteBook(id, position)
                                    Movie::class.java -> deleteEntryViewModel.deleteMovie(id, position)
                                    Documentary::class.java -> deleteEntryViewModel.deleteDocumentary(id, position)
                                    Game::class.java -> deleteEntryViewModel.deleteGame(id, position)
                                    else -> throw IllegalStateException()
                                }
                            }.show()
                        true
                    }
                    it.setIcon(R.drawable.baseline_delete_24)
                }
            }
            show()
        }
    }
}
