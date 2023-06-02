@file:Suppress("DEPRECATION")

package dev.liinahamari.highlights.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.liinahamari.highlights.App
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.databinding.FragmentCategoryBinding

class EntryFragment : Fragment(R.layout.fragment_category) {
    private val ui by viewBinding(FragmentCategoryBinding::bind)
    private val entriesViewModel: EntriesViewModel by lazy { EntriesViewModel((requireActivity().application as App).db)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewModelSubscriptions()
        setupFab()
        entriesViewModel.fetchEntries(
            requireArguments().getParcelable(ARG_TYPE)!!,
            requireArguments().getParcelable(ARG_CATEGORY)!!
        )
    }

    private fun setupViewModelSubscriptions() {
        entriesViewModel.saveEvent.observe(viewLifecycleOwner) {
            entriesViewModel.fetchEntries(
                requireArguments().getParcelable(ARG_TYPE)!!,
                requireArguments().getParcelable(ARG_CATEGORY)!!
            )
        }
        entriesViewModel.fetchEvent.observe(viewLifecycleOwner) {
            if (it is EntriesViewModel.FetchEvent.SUCCESSFUL) {
                ui.entriesRv.adapter = EntryAdapter(it.entries).apply { notifyDataSetChanged() }
            }
        }
    }

    private fun setupFab() {
        ui.fab.setOnClickListener {
            @Suppress("DEPRECATION")
            when (requireArguments().getParcelable<EntityType>(ARG_TYPE)) {
                EntityType.BOOK -> showAddBookDialog(
                    requireArguments().getParcelable(ARG_CATEGORY)!!,
                    entriesViewModel::saveBook
                )

                EntityType.GAME -> showAddGameDialog(
                    requireArguments().getParcelable(ARG_CATEGORY)!!,
                    entriesViewModel::saveGame
                )

                EntityType.MOVIE -> showAddMovieDialog(
                    requireArguments().getParcelable(ARG_CATEGORY)!!,
                    entriesViewModel::saveMovie
                )

                EntityType.DOCUMENTARY -> showAddDocumentaryDialog(
                    requireArguments().getParcelable(ARG_CATEGORY)!!,
                    entriesViewModel::saveDocumentary
                )

                else -> throw IllegalStateException()
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
