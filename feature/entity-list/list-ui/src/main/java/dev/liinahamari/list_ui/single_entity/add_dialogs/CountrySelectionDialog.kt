package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.CountriesRvItemBinding
import java.util.Locale
import java.util.Locale.getISOCountries

//todo move to first launch and cache
//todo loading state while searching
fun Fragment.showCountrySelectionDialog(preselectedLocales: List<String>, onCountrySelected: (List<String>) -> Unit) {
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(CountriesRvItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(filteredLocales[position], onChecked, preselectedLocales)

    override fun getItemCount(): Int = filteredLocales.size

    class ViewHolder(private val ui: CountriesRvItemBinding) : RecyclerView.ViewHolder(ui.root) {
        fun bind(
            locale: Locale,
            onChecked: (isChecked: Boolean, countryCode: String) -> Unit,
            preselectedLocales: List<String>
        ) {
            ui.root.setOnClickListener { ui.checkbox.isChecked = ui.checkbox.isChecked.not() }
            ui.titleTv.text = locale.displayCountry

            ui.checkbox.isChecked = locale.country in preselectedLocales
            ui.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onChecked(isChecked, locale.country)
            }
        }
    }
}
