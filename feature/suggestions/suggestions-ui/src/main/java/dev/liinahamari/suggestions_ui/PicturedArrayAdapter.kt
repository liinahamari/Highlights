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
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.core.ext.layoutInflater

class PicturedArrayAdapter(private val context: Context) :
    ArrayAdapter<SuggestionUi>(context, R.layout.suggestions_list_item) {
    private inner class ViewHolder(view: View) {
        var veilLayout: VeilLayout = view.findViewById(R.id.veilLayout)
        var thumbIv: ImageView = view.findViewById(R.id.thumbnail)
        var titleTv: TextView = view.findViewById(R.id.titleTv)
        var yearTv: TextView = view.findViewById(R.id.yearTv)
        var genresTv: TextView = view.findViewById(R.id.genresTv)
    }

    init {
        setNotifyOnChange(true)
    }

    fun replaceAll(dataset: List<SuggestionUi>) {
        clear()
        addAll(dataset)
        filter.filter(null)
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
        context.layoutInflater.inflate(R.layout.suggestions_list_item, null).apply {
            ViewHolder(this).apply {
                with(getItem(position)!!) {
                    titleTv.text = title
                    yearTv.text = year.toString()
                    genresTv.text = genres.joinToString()
                    posterUrl?.let {
                        Glide.with(context)
                            .load(getItem(position)?.posterUrl)
//                          .load(/*"https://image.tmdb.org/t/p/w154${*/getItem(position)?.posterUrl/*}"*/)
                            .fallback(android.R.drawable.gallery_thumb)
                            .error(android.R.drawable.gallery_thumb)
                            .timeout(10_000)
                            .override(100, 100)
                            .listener(UnveilRequestListener { veilLayout.unVeil() })
                            .into(thumbIv)
                    }
                }
            }
        }
}

data class SuggestionUi(val title: String, val year: Int, val posterUrl: String?, val genres: List<String>)

fun Movie.toUi() = SuggestionUi(
    title = name,
    year = year,
    genres = genres.map { it.name.replace("_", " ").lowercase() },
    posterUrl = posterUrl
)

fun Book.toUi() = SuggestionUi(
    title = name,
    year = year,
    posterUrl = posterUrl,
    genres = genres.map { it.name.replace("_", " ").lowercase() }
)

fun Game.toSuggestion() = SuggestionUi(
    title = name,
    year = year,
    posterUrl = posterUrl,
    genres = genres.map { it.name.replace("_", " ").lowercase() }
)
