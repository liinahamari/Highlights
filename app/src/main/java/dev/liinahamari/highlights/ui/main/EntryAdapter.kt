package dev.liinahamari.highlights.ui.main

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.liinahamari.highlights.PopupImage
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.inflate


data class Entry(
    val description: String, val url: String
)

class EntryAdapter(private val dataSet: List<Entry>) : RecyclerView.Adapter<EntryAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTv: TextView
        val posterIv: ImageView

        init {
            nameTv = view.findViewById(R.id.entryNameTv)
            posterIv = view.findViewById(R.id.posterIv)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(viewGroup.inflate(R.layout.entry_row_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.nameTv.text = dataSet[position].description
        Glide.with(viewHolder.posterIv.context)
            .load(dataSet[position].url)
            .timeout(20_000)
            .into(viewHolder.posterIv)
        viewHolder.posterIv.setOnClickListener {
            PopupImage(it.context)
                .show(dataSet[position].url, it)
        }
    }

    override fun getItemCount() = dataSet.size
}
