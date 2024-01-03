@file:Suppress("NAME_SHADOWING")

package dev.liinahamari.suggestions_ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dev.liinahamari.suggestions.api.model.RemoteMovie

class PicturedArrayAdapter(private val context: Context) :
    ArrayAdapter<RemoteMovie>(context, R.layout.suggestions_list_item, emptyArray()) {
    private inner class ViewHolder {
        var thumbIv: ImageView? = null
        var titleTv: TextView? = null
    }

    init {
        setNotifyOnChange(true)
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var holder: ViewHolder
        var convertView = convertView
        if (convertView == null) {
            with(
                (context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.suggestions_list_item,
                    null
                )
            ) {
                convertView = this
                holder = ViewHolder()
                holder.titleTv = findViewById(R.id.titleTv)
                holder.thumbIv = findViewById(R.id.thumbnail)
                tag = holder
            }
        } else {
            holder = convertView!!.tag as ViewHolder
        }
        holder.titleTv!!.text = getItem(position)!!.title
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w154${getItem(position)?.posterPath}")
            .fallback(android.R.drawable.gallery_thumb)
            .error(android.R.drawable.gallery_thumb)
            .timeout(10_000)
            .override(100, 100)
            .into(holder.thumbIv!!)
        return convertView!!
    }
}
