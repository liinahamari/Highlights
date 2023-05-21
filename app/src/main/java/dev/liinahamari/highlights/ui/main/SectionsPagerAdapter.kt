package dev.liinahamari.highlights.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

private val TAB_TITLES =
    arrayOf(EntityCategory.GOOD.toString(), EntityCategory.BAD.toString(), EntityCategory.WISHLIST.toString())

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = ViewPagerPlaceholderFragment.newInstance(TAB_TITLES[position])
    override fun getPageTitle(position: Int): CharSequence = TAB_TITLES[position]
    override fun getCount(): Int = TAB_TITLES.size
}
