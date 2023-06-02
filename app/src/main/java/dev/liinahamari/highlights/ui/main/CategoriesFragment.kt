package dev.liinahamari.highlights.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    private val ui: FragmentCategoriesBinding by viewBinding(FragmentCategoriesBinding::bind)
    companion object {
        const val ARG_CATEGORY = "arg_category"
        @JvmStatic fun newInstance(category: EntityCategory) =
            CategoriesFragment().apply { arguments = bundleOf(ARG_CATEGORY to category) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        EntityType.values().map { it.toString() }.forEach { entityType ->
            ui.categoriesContainer.addView(Button(requireContext()).apply {
                text = entityType
                setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .add(
                            R.id.pagerContainer,
                            EntryFragment.newInstance(
                                @Suppress("DEPRECATION") requireArguments().getParcelable(ARG_CATEGORY)!!,
                                EntityType.valueOf(entityType)
                            )
                        )
                        .addToBackStack("123")
                        .commit()
                }
            })
        }
    }
}
