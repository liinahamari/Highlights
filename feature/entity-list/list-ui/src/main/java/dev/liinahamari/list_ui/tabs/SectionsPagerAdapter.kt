package dev.liinahamari.list_ui.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.liinahamari.list_ui.single_entity.EntityCategory

val TAB_TITLES = arrayOf(EntityCategory.GOOD, EntityCategory.SO_SO, EntityCategory.WISH_LIST)

class SectionsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = TAB_TITLES.size
    override fun createFragment(position: Int): Fragment =
        ViewPagerPlaceholderFragment.newInstance(TAB_TITLES[position])
}
