package dev.liinahamari.highlights.ui.main

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import dev.liinahamari.highlights.ui.OnBackPressedListener
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.ui.main.EntryFragment.Companion.ARG_CATEGORY

class ViewPagerPlaceholderFragment : Fragment(R.layout.fragment_main), OnBackPressedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.beginTransaction()
            .replace(R.id.pagerContainer, CategoriesFragment.newInstance(requireArguments().getParcelable(ARG_CATEGORY)!!))
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
