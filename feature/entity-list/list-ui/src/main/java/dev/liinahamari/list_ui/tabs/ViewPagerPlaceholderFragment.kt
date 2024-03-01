package dev.liinahamari.list_ui.tabs

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.entries_list.EntriesFragment
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY

class ViewPagerPlaceholderFragment : Fragment(R.layout.fragment_main), MainActivity.OnBackPressedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.beginTransaction()
            .replace(
                R.id.pagerContainer,
                EntriesFragment.newInstance(requireArguments().getParcelableOf(ARG_CATEGORY))
            )
            .commit()
    }

    companion object {
        @JvmStatic fun newInstance(category: Category): ViewPagerPlaceholderFragment =
            ViewPagerPlaceholderFragment().apply { arguments = bundleOf(ARG_CATEGORY to category) }
    }

    override fun onBackPressed() {
        if (childFragmentManager.fragments.isNotEmpty()) {
            childFragmentManager.popBackStack()
        }
    }
}
