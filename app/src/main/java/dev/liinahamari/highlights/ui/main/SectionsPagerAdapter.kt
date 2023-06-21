package dev.liinahamari.highlights.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

val TAB_TITLES = arrayOf(EntityCategory.GOOD, EntityCategory.SO_SO, EntityCategory.WISHLIST)
class SectionsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = TAB_TITLES.size
    override fun createFragment(position: Int): Fragment = ViewPagerPlaceholderFragment.newInstance(TAB_TITLES[position])
}
