package dev.liinahamari.highlights.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.liinahamari.highlights.R

class EntryAdapter(private val dataSet: List<String>) : RecyclerView.Adapter<EntryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTv: TextView

        init {
            nameTv = view.findViewById(R.id.entryNameTv)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.entry_row_item, viewGroup, false)
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.nameTv.text = dataSet[position]
    }

    override fun getItemCount() = dataSet.size
}
