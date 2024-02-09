package dev.liinahamari.suggestions_ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.skydoves.androidveil.VeilLayout
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.core.ext.layoutInflater

class PicturedArrayAdapter(private val context: Context) :
    ArrayAdapter<Movie>(context, R.layout.suggestions_list_item, emptyArray()) {
    private inner class ViewHolder(view: View) {
        var veilLayout: VeilLayout = view.findViewById(R.id.veilLayout)
        var thumbIv: ImageView = view.findViewById(R.id.thumbnail)
        var titleTv: TextView = view.findViewById(R.id.titleTv)
    }

    init {
        setNotifyOnChange(true)
    }

    fun replaceAll(movies: List<Movie>) {
        clear()
        addAll(movies)
        filter.filter(null)
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
        context.layoutInflater.inflate(R.layout.suggestions_list_item, null).apply {
            ViewHolder(this).apply {
                titleTv.text = getItem(position)!!.name
                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w154${getItem(position)?.posterUrl}")
                    .fallback(android.R.drawable.gallery_thumb)
                    .error(android.R.drawable.gallery_thumb)
                    .timeout(10_000)
                    .override(100, 100)
//                    .listener(UnveilRequestListener { veilLayout.unVeil() })
                    .into(thumbIv)
            }
        }
}
