package dev.liinahamari.highlights.ui.single_entity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.databinding.FragmentCategoryBinding
import dev.liinahamari.highlights.helper.appComponent
import dev.liinahamari.highlights.helper.getParcelableOf
import dev.liinahamari.highlights.ui.single_entity.EntityType.BOOK
import dev.liinahamari.highlights.ui.single_entity.EntityType.DOCUMENTARY
import dev.liinahamari.highlights.ui.single_entity.EntityType.GAME
import dev.liinahamari.highlights.ui.single_entity.EntityType.MOVIE
import javax.inject.Inject

class EntryFragment : Fragment(R.layout.fragment_category), LongClickListener {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val ui by viewBinding(FragmentCategoryBinding::bind)
    private val entryViewModel: EntryViewModel by viewModels { viewModelFactory }

    private val argumentEntityType: EntityType by lazy { requireArguments().getParcelableOf(ARG_TYPE) }
    private val argumentEntityCategory: EntityCategory by lazy { requireArguments().getParcelableOf(ARG_CATEGORY) }

    override fun onAttach(context: Context) {
        appComponent?.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewModelSubscriptions()
        setupFab()
        entryViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
    }

    private fun setupViewModelSubscriptions() {
        entryViewModel.saveEvent.observe(viewLifecycleOwner) {
            entryViewModel.fetchEntries(argumentEntityType, argumentEntityCategory)
        }
        entryViewModel.fetchEvent.observe(viewLifecycleOwner) {
            if (it is EntryViewModel.FetchEvent.Success) {
                ui.entriesRv.adapter = EntryAdapter(it.entries, this).apply { notifyDataSetChanged() }
            }
        }
    }

    private fun setupFab() {
        ui.fab.setOnClickListener {
            when (argumentEntityType) {
                BOOK -> showAddBookDialog(argumentEntityCategory, entryViewModel::saveBook)
                GAME -> showAddGameDialog(argumentEntityCategory, entryViewModel::saveGame)
                MOVIE -> showAddMovieDialog(argumentEntityCategory, entryViewModel::saveMovie)
                DOCUMENTARY -> showAddDocumentaryDialog(argumentEntityCategory, entryViewModel::saveDocumentary)
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
}
