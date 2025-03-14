package dev.liinahamari.list_ui.entries_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.liinahamari.list_ui.databinding.ButtonItemBinding
import dev.liinahamari.list_ui.di.ViewModelBuilderModule

class EntityButtonsAdapter(private val onEntityClick: (ViewModelBuilderModule.ENTITY_TYPE) -> Unit) :
    RecyclerView.Adapter<EntityButtonsAdapter.ViewHolder>() {
    private val entities = ViewModelBuilderModule.ENTITY_TYPE.entries.toTypedArray()

    inner class ViewHolder(private val ui: ButtonItemBinding) : RecyclerView.ViewHolder(ui.root) {
        fun bind(buttonTitle: String) {
            with(ui.entryBtn) {
                text = buttonTitle
                setOnClickListener { onEntityClick.invoke(entities[bindingAdapterPosition]) }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ButtonItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(entities[position].toString())

    override fun getItemCount() = entities.size
}
