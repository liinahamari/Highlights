package dev.liinahamari.highlights.ui.main

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import dev.liinahamari.highlights.IOnBackPressed
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.ui.main.EntryFragment.Companion.ARG_ENTITY_TYPE

class ViewPagerPlaceholderFragment : Fragment(R.layout.fragment_main), IOnBackPressed {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.beginTransaction()
            .replace(R.id.pagerContainer, CategoriesFragment.newInstance(requireArguments().getString(ARG_ENTITY_TYPE)!!))
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryName: String): ViewPagerPlaceholderFragment = ViewPagerPlaceholderFragment().apply {
            arguments = bundleOf(EntryFragment.ARG_ENTITY_TYPE to categoryName)
        }
    }

    override fun onBackPressed() {
        if (childFragmentManager.fragments.isNotEmpty()) {
            childFragmentManager.popBackStack()
        }
    }
}
