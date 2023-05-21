package dev.liinahamari.highlights.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.db.EntriesDatabase

class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    val category: EntityCategory by lazy { EntityCategory.valueOf(requireArguments().getString(ARG_CATEGORY)!!) }

    companion object {
        const val ARG_CATEGORY = "arg_category"
        @JvmStatic fun newInstance(categoryName: String) =
            CategoriesFragment().apply { arguments = bundleOf(ARG_CATEGORY to categoryName) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        EntriesDatabase::class.java.declaredMethods.map { it.name }.forEach { entityType ->
            view.findViewById<LinearLayout>(R.id.categoriesContainer).addView(Button(requireContext()).apply {
                text = entityType.dropLast(3)
                setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .add(R.id.pagerContainer, EntryFragment.newInstance(entityType, category.name))
                        .addToBackStack("123")
                        .commit()
                }
            })
        }
    }
}
