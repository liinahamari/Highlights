package dev.liinahamari.suggestions.sample.ui.main

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.suggestions.sample.R
import dev.liinahamari.suggestions_ui.ARG_CATEGORY

class MainFragment : Fragment(R.layout.fragment_main) {
    companion object {
        fun newInstance(category: Category) = MainFragment().apply { arguments = bundleOf(ARG_CATEGORY to category) }
    }
}
