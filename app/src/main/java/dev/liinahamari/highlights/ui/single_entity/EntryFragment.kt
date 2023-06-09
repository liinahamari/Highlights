package dev.liinahamari.highlights.ui.single_entity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.databinding.FragmentCategoryBinding
import dev.liinahamari.highlights.db.daos.Book
import dev.liinahamari.highlights.db.daos.Documentary
import dev.liinahamari.highlights.db.daos.Game
import dev.liinahamari.highlights.db.daos.Movie
import dev.liinahamari.highlights.helper.getParcelableOf
import dev.liinahamari.highlights.ui.MainActivity
import dev.liinahamari.highlights.ui.single_entity.EntityType.BOOK
import dev.liinahamari.highlights.ui.single_entity.EntityType.DOCUMENTARY
import dev.liinahamari.highlights.ui.single_entity.EntityType.GAME
import dev.liinahamari.highlights.ui.single_entity.EntityType.MOVIE
import javax.inject.Inject

class EntryFragment : Fragment(R.layout.fragment_category), LongClickListener {
    private val ui by viewBinding(FragmentCategoryBinding::bind)

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val fetchEntriesViewModel: FetchEntriesViewModel by viewModels { viewModelFactory }
    private val saveEntryViewModel: SaveEntryViewModel by viewModels { viewModelFactory }
    private val deleteEntryViewModel: DeleteEntryViewModel by viewModels { viewModelFactory }

    private val argumentEntityType: EntityType by lazy { requireArguments().getParcelableOf(ARG_TYPE) }
    private val argumentEntityCategory: EntityCategory by lazy { requireArguments().getParcelableOf(ARG_CATEGORY) }

    override fun onAttach(context: Context) {
        (requireActivity() as MainActivity).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        fetchEntriesViewModel.fetchEvent.observe(viewLifecycleOwner) {
            if (it is FetchEntriesViewModel.FetchEvent.Success) {
                ui.entriesRv.adapter = EntryAdapter(it.entries.toMutableList(), this)
                    .apply { notifyDataSetChanged() }
            }
        }
    }

    private fun setupFab() {
        ui.fab.setOnClickListener {
            when (argumentEntityType) {
                BOOK -> showAddBookDialog(argumentEntityCategory, saveEntryViewModel::saveBook)
                GAME -> showAddGameDialog(argumentEntityCategory, saveEntryViewModel::saveGame)
                MOVIE -> showAddMovieDialog(argumentEntityCategory, saveEntryViewModel::saveMovie)
                DOCUMENTARY -> showAddDocumentaryDialog(argumentEntityCategory, saveEntryViewModel::saveDocumentary)
            }
        }
    }

    companion object {
        const val ARG_CATEGORY = "arg_category"
        const val ARG_TYPE = "arg_type"
        @JvmStatic fun newInstance(entityCategory: EntityCategory, entityType: EntityType) =
            EntryFragment().apply {
                arguments = bundleOf(ARG_CATEGORY to entityCategory, ARG_TYPE to entityType)
            }
    }

    override fun onLongClicked(id: String, clazz: Class<*>, position: Int) {
        MaterialDialog(requireContext())
            .message(R.string.sure_to_delete)
            .negativeButton(R.string.yes)
            .positiveButton {
                when (clazz) {
                    Book::class.java -> deleteEntryViewModel.deleteBook(id, position)
                    Movie::class.java -> deleteEntryViewModel.deleteMovie(id, position)
                    Documentary::class.java -> deleteEntryViewModel.deleteDocumentary(id, position)
                    Game::class.java -> deleteEntryViewModel.deleteGame(id, position)
                    else -> throw IllegalStateException()
                }
            }.show()
    }
}
