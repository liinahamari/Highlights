package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.annotation.SuppressLint
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import dev.liinahamari.api.domain.entities.Country
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.CountriesRvItemBinding
import dev.liinahamari.list_ui.databinding.CountriesRvItemBinding.inflate

//todo loading state while searching
fun Fragment.showCountrySelectionDialog(
    countries: List<Country>,
    preselectedLocales: List<Country> = emptyList(),
    onCountrySelected: (List<Country>) -> Unit
) {
    val countriesListAdapter = CountriesAdapter(
        allLocales = countries,
        preselectedLocales = preselectedLocales
    )
    MaterialDialog(requireContext())
        .positiveButton(res = android.R.string.ok) { onCountrySelected(countriesListAdapter.checkedLocales + preselectedLocales) }
        .customView(R.layout.countries_selection)
        .apply {
            getCustomView().also {
                it.findViewById<RecyclerView>(R.id.countriesRv).apply {
                    this.adapter = countriesListAdapter
                }
                findViewById<SearchView>(R.id.searchView).setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean = false
                    override fun onQueryTextChange(newText: String) =
                        false.also { countriesListAdapter.filter(newText) }
                })
            }
        }
        .show()
}

private class CountriesAdapter(
    private val allLocales: List<Country>,
    private val preselectedLocales: List<Country>
) : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {
    class ViewHolder(val ui: CountriesRvItemBinding) : RecyclerView.ViewHolder(ui.root)

    private val asyncListDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean = oldItem.iso == newItem.iso
        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean = oldItem == newItem
    })

    private var filteredLocales: List<Country> = allLocales
    val checkedLocales = mutableListOf<Country>()

    init {
        filteredLocales = allLocales
        asyncListDiffer.submitList(filteredLocales)
    }

    @SuppressLint("NotifyDataSetChanged") fun filter(text: String) {
        filteredLocales = if (text.isBlank()) {
            allLocales
        } else {
            allLocales.filter { it.name.contains(text, true) }
        }
        asyncListDiffer.submitList(filteredLocales)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(inflate(from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ui.checkbox.setOnCheckedChangeListener(null)
        with(asyncListDiffer.currentList[position]) {
            holder.ui.titleTv.text = name
            holder.ui.checkbox.isChecked = checkedLocales.contains(this) || this in preselectedLocales
            holder.ui.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (checkedLocales.contains(this).not()) {
                        checkedLocales.add(this)
                    }
                } else {
                    checkedLocales.remove(this)
                }
            }
        }
    }
}
