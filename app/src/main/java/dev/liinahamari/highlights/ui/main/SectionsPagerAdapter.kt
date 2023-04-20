package dev.liinahamari.highlights.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import dev.liinahamari.highlights.R

private val TAB_TITLES = arrayOf(R.string.tab_text_1, R.string.tab_text_2)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = ViewPagerPlaceholderFragment.newInstance()
    override fun getPageTitle(position: Int): CharSequence = context.resources.getString(TAB_TITLES[position])
    override fun getCount(): Int = TAB_TITLES.size
}
