package dev.liinahamari.highlights.ui.entries_list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.databinding.FragmentEntriesBinding
import dev.liinahamari.highlights.helper.getParcelableOf
import dev.liinahamari.highlights.ui.single_entity.EntityCategory
import dev.liinahamari.highlights.ui.single_entity.EntityType
import dev.liinahamari.highlights.ui.single_entity.EntryFragment

class EntriesFragment : Fragment(R.layout.fragment_entries) {
    private val ui: FragmentEntriesBinding by viewBinding(FragmentEntriesBinding::bind)

    companion object {
        const val ARG_CATEGORY = "arg_category"
        @JvmStatic fun newInstance(category: EntityCategory) =
            EntriesFragment().apply { arguments = bundleOf(ARG_CATEGORY to category) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ui.entityButtonRv.adapter = EntityButtonsAdapter(::onEntityClicked)
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
