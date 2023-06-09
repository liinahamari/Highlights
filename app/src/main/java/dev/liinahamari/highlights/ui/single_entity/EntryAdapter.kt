package dev.liinahamari.highlights.ui.single_entity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.liinahamari.highlights.custom_views.PopupImage
import dev.liinahamari.highlights.databinding.EntryRowItemBinding


data class Entry(val id: String, val description: String, val url: String, val clazz: Class<*>)

private const val TIMEOUT_20_SEC = 20_000

interface LongClickListener {
    fun onLongClicked(id: String, clazz: Class<*>, position: Int)
}

class EntryAdapter(val dataSet: MutableList<Entry>, private val longClickListener: LongClickListener) :
    RecyclerView.Adapter<EntryAdapter.ViewHolder>() {
    inner class ViewHolder(private val ui: EntryRowItemBinding) : RecyclerView.ViewHolder(ui.root) {
        fun bind(entry: Entry) {
            ui.root.setOnLongClickListener {
                longClickListener.onLongClicked(entry.id, entry.clazz, bindingAdapterPosition)
                true
            }
            ui.entryNameTv.text = entry.description
            Glide.with(ui.posterIv.context)
                .load(entry.url)
                .timeout(TIMEOUT_20_SEC)
                .into(ui.posterIv)
            ui.posterIv.setOnClickListener {
                PopupImage(it.context).show(entry.url, it)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(EntryRowItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(dataSet[position])
    override fun getItemCount() = dataSet.size
}
