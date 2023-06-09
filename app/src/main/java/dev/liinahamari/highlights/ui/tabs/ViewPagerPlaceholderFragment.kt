package dev.liinahamari.highlights.ui.tabs

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.helper.getParcelableOf
import dev.liinahamari.highlights.ui.OnBackPressedListener
import dev.liinahamari.highlights.ui.single_entity.EntityCategory
import dev.liinahamari.highlights.ui.single_entity.EntryFragment.Companion.ARG_CATEGORY
import dev.liinahamari.highlights.ui.entries_list.EntriesFragment

class ViewPagerPlaceholderFragment : Fragment(R.layout.fragment_main), OnBackPressedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.beginTransaction()
            .replace(R.id.pagerContainer, EntriesFragment.newInstance(requireArguments().getParcelableOf(ARG_CATEGORY)))
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(category: EntityCategory): ViewPagerPlaceholderFragment = ViewPagerPlaceholderFragment().apply {
            arguments = bundleOf(ARG_CATEGORY to category)
        }
    }

    override fun onBackPressed() {
        if (childFragmentManager.fragments.isNotEmpty()) {
            childFragmentManager.popBackStack()
        }
    }
}
