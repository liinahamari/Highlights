package dev.liinahamari.highlights.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.liinahamari.highlights.custom_views.PopupImage
import dev.liinahamari.highlights.databinding.EntryRowItemBinding


data class Entry(val description: String, val url: String)

class EntryAdapter(private val dataSet: List<Entry>) : RecyclerView.Adapter<EntryAdapter.ViewHolder>() {
    class ViewHolder(private val ui: EntryRowItemBinding) : RecyclerView.ViewHolder(ui.root) {
        fun bind(entry: Entry) {
            ui.entryNameTv.text = entry.description
            Glide.with(ui.posterIv.context)
                .load(entry.url)
                .timeout(20_000)
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
