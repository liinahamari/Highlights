package dev.liinahamari.highlights.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.db.EntriesDatabase

class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        EntriesDatabase::class.java.declaredMethods.map { it.name }.forEach { categoryName ->
            view.findViewById<LinearLayout>(R.id.categoriesContainer).addView(Button(requireContext()).apply {
                text = categoryName.dropLast(3)
                setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .add(R.id.pagerContainer, CategoryFragment.newInstance(categoryName))
                        .addToBackStack("123")
                        .commit()
                }
            })
        }
    }
}
