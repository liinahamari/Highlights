package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import dev.liinahamari.core.ext.inflate
import dev.liinahamari.list_ui.R
import java.util.Locale
import java.util.Locale.getISOCountries

//todo move to first launch and cache
//todo loading state while searching
fun Context.showCountrySelectionDialog(preselectedLocales: List<String>, onCountrySelected: (List<String>) -> Unit) {
    val allLocales = getISOCountries().map { Locale("", it) }.sortedBy { it.displayCountry }
    val chosenLocales = preselectedLocales.toMutableList()
    val adapter = CountriesAdapter(allLocales, preselectedLocales) { isChecked: Boolean, countryCode: String ->
        if (isChecked) {
            if (chosenLocales.contains(countryCode).not()) chosenLocales.add(countryCode)
        } else chosenLocales.remove(countryCode)
    }

    MaterialDialog(requireContext())
        .positiveButton(res = android.R.string.ok) {
            onCountrySelected(chosenLocales)
        }
        .customView(R.layout.countries_selection)
        .apply {
            getCustomView().also {
                it.findViewById<RecyclerView>(R.id.countriesRv).apply {
                    setHasFixedSize(true)
                    this.adapter = adapter
                }
                findViewById<SearchView>(R.id.searchView).setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean = false
                    override fun onQueryTextChange(newText: String) = false.also { adapter.filter(newText) }
                })
            }
        }
        .show()
}

private class CountriesAdapter(
    private val allLocales: List<Locale>,
    private val preselectedLocales: List<String>,
    private val onChecked: (isChecked: Boolean, countryCode: String) -> Unit
) : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {
    private var filteredLocales: List<Locale> = allLocales

    @SuppressLint("NotifyDataSetChanged") fun filter(text: String) {
        filteredLocales = allLocales.filter { it.displayCountry.contains(text, true) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.countries_rv_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            holder.checkBox.isChecked = holder.checkBox.isChecked.not()
        }
        holder.countryTitleTv.text = filteredLocales[position].displayCountry

        holder.checkBox.isChecked = filteredLocales[position].country in preselectedLocales
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onChecked(isChecked, filteredLocales[position].country)
        }
    }

    override fun getItemCount(): Int = filteredLocales.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countryTitleTv: TextView = itemView.findViewById(R.id.titleTv)
        var checkBox: AppCompatCheckBox = itemView.findViewById(R.id.checkbox)
    }
}
