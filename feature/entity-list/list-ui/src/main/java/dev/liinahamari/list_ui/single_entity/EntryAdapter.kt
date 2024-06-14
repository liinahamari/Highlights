package dev.liinahamari.list_ui.single_entity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.liinahamari.api.domain.entities.EntryUi
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.custom_views.PopupImageDialog
import dev.liinahamari.list_ui.databinding.EntryRowItemBinding
import net.cachapa.expandablelayout.ExpandableLayout.OnExpansionUpdateListener

private const val TIMEOUT_20_SEC = 20_000

class EntryAdapter(
    private val fragmentManager: FragmentManager,
    private val recyclerView: RecyclerView
) :
    RecyclerView.Adapter<EntryAdapter.ViewHolder>() {
    private var entries: MutableList<EntryUi> = mutableListOf()
    private var filteredEntries: MutableList<EntryUi> = entries

    private val UNSELECTED = -1
    private var expandedItems = mutableListOf<Int>()
    private var selectedItem = UNSELECTED //fixme filtered

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = filteredEntries[position].id

    @SuppressLint("NotifyDataSetChanged") fun replaceDataset(dataSet: List<EntryUi>) {
        this.entries = dataSet.toMutableList()
        this.filteredEntries = dataSet.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        filteredEntries.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(private val ui: EntryRowItemBinding) : RecyclerView.ViewHolder(ui.root),
        OnExpansionUpdateListener {

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> = object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = bindingAdapterPosition
            override fun getSelectionKey(): Long = itemId
        }

        fun bind(entry: EntryUi, isActivated: Boolean = false) {
            ui.expandableLayout.apply {
                setInterpolator(OvershootInterpolator())
                setOnExpansionUpdateListener(this@ViewHolder)
                isExpanded = bindingAdapterPosition in expandedItems
            }

            itemView.apply {
                this.isActivated = isActivated
                setOnClickListener {
                    if (recyclerView.findViewHolderForAdapterPosition(expandedItems.firstOrNull { it == bindingAdapterPosition }
                            ?: UNSELECTED) as ViewHolder? != null) {
                        ui.expandableLayout.collapse(false)
                    }

                    if (bindingAdapterPosition in expandedItems) {
                        expandedItems.remove(bindingAdapterPosition)
                    } else {
                        ui.expandableLayout.expand()
                        expandedItems.add(bindingAdapterPosition)
                    }
                }
            }

            ui.titleTv.text = entry.title
            ui.yearTv.text = ui.root.context.getString(R.string.years_placeholder, entry.year)
            ui.entryGenresTv.text = entry.genres
            ui.entryCountriesTv.text = ui.root.context.getString(R.string.countries_placeholder, entry.countries)
            ui.entryDescriptionTv.text = entry.description

            Glide.with(ui.posterIv.context)
                .load(entry.url)
                .timeout(TIMEOUT_20_SEC)
                .into(ui.posterIv)
            ui.posterIv.setOnClickListener {
                PopupImageDialog(entry.url!!).show(fragmentManager, null)
            }
        }

        override fun onExpansionUpdate(expansionFraction: Float, state: Int) = Unit
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(EntryRowItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(filteredEntries[position], tracker!!.isSelected(filteredEntries[position].id))
    }

    override fun getItemCount() = filteredEntries.size

    @SuppressLint("NotifyDataSetChanged") fun filter(text: String) {
        filteredEntries = entries.filter { it.title.contains(text, true) }.toMutableList()
        notifyDataSetChanged()
    }

    class EntryDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? =
            recyclerView.findChildViewUnder(event.x, event.y)?.let {
                (recyclerView.getChildViewHolder(it) as EntryAdapter.ViewHolder)
                    .getItemDetails()
            }
    }
}
