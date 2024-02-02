package dev.liinahamari.list_ui.single_entity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.liinahamari.list_ui.custom_views.PopupImageDialog
import dev.liinahamari.list_ui.databinding.EntryRowItemBinding

data class Entry(
    val id: Long,
    val title: String,
    val countries: String,
    val genres: String,
    val description: String,
    val url: String?,
    val clazz: Class<*>
)

private const val TIMEOUT_20_SEC = 20_000

interface LongClickListener {
    fun onLongClicked(id: Long, clazz: Class<*>, position: Int)
}

class EntryAdapter(
    private val longClickListener: LongClickListener,
    private val fragmentManager: FragmentManager
) :
    RecyclerView.Adapter<EntryAdapter.ViewHolder>() {
    private var entries: MutableList<Entry> = mutableListOf()
    private var filteredEntries: MutableList<Entry> = entries

    @SuppressLint("NotifyDataSetChanged") fun replaceDataset(dataSet: List<Entry>) {
        this.entries = dataSet.toMutableList()
        this.filteredEntries = dataSet.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        filteredEntries.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(private val ui: EntryRowItemBinding) : RecyclerView.ViewHolder(ui.root) {
        fun bind(entry: Entry) {
            ui.root.setOnLongClickListener {
                longClickListener.onLongClicked(entry.id, entry.clazz, bindingAdapterPosition)
                true
            }
            ui.entryNameTv.text = entry.title
            ui.entryGenresTv.text = entry.genres
            ui.entryCountriesTv.text = entry.countries
            ui.entryDescriptionTv.text = entry.description

            Glide.with(ui.posterIv.context)
                .load(entry.url)
                .timeout(TIMEOUT_20_SEC)
                .into(ui.posterIv)
            ui.posterIv.setOnClickListener {
                PopupImageDialog(entry.url!!).show(fragmentManager, null)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(EntryRowItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(filteredEntries[position])
    override fun getItemCount() = filteredEntries.size

    @SuppressLint("NotifyDataSetChanged") fun filter(text: String) {
        filteredEntries = entries.filter { it.title.contains(text, true) }.toMutableList()
        notifyDataSetChanged()
    }
}
