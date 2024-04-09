package dev.liinahamari.list_ui.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val tabs = ViewPagerPlaceholderFragment.ViewPagerEntries.values()
    override fun getItemCount(): Int = tabs.size
    override fun createFragment(position: Int): Fragment = ViewPagerPlaceholderFragment.newInstance(tabs[position])
}
