package dev.liinahamari.highlights.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import dev.liinahamari.highlights.IOnBackPressed
import dev.liinahamari.highlights.R

class ViewPagerPlaceholderFragment : Fragment(R.layout.fragment_main), IOnBackPressed {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.beginTransaction().replace(R.id.pagerContainer, CategoriesFragment()).commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(): ViewPagerPlaceholderFragment = ViewPagerPlaceholderFragment()
    }

    override fun onBackPressed() {
        if (childFragmentManager.fragments.isNotEmpty()) {
            childFragmentManager.popBackStack()
        }
    }
}
