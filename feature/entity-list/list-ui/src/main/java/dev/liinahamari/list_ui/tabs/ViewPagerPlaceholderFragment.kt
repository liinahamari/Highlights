package dev.liinahamari.list_ui.tabs

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.entries_list.EntriesFragment
import dev.liinahamari.list_ui.single_entity.SettingsFragment
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY
import kotlinx.parcelize.Parcelize

class ViewPagerPlaceholderFragment : Fragment(R.layout.fragment_main), MainActivity.OnBackPressedListener {
    @Parcelize
    enum class ViewPagerEntries(val emoji: String) : Parcelable {
        CATEGORY_GOOD("👍"), CATEGORY_SO_SO("👎"), CATEGORY_WISHLIST("🌠"), SETTINGS("⚙️")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = when (val viewPagerEntry: ViewPagerEntries = requireArguments().getParcelableOf(ARG_CATEGORY)) {
            ViewPagerEntries.SETTINGS -> SettingsFragment.newInstance()
            ViewPagerEntries.CATEGORY_GOOD, ViewPagerEntries.CATEGORY_SO_SO, ViewPagerEntries.CATEGORY_WISHLIST -> EntriesFragment.newInstance(
                when (viewPagerEntry) {
                    ViewPagerEntries.CATEGORY_GOOD -> Category.GOOD
                    ViewPagerEntries.CATEGORY_SO_SO -> Category.SO_SO
                    ViewPagerEntries.CATEGORY_WISHLIST -> Category.WISHLIST
                    else -> throw IllegalStateException()
                }
            )
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.pagerContainer, fragment)
            .commit()
    }

    companion object {
        @JvmStatic fun newInstance(viewPagerEntry: ViewPagerEntries): ViewPagerPlaceholderFragment =
            ViewPagerPlaceholderFragment().apply { arguments = bundleOf(ARG_CATEGORY to viewPagerEntry) }
    }

    override fun onBackPressed() {
        if (childFragmentManager.fragments.isNotEmpty()) {
            childFragmentManager.popBackStack()
        }
    }
}
