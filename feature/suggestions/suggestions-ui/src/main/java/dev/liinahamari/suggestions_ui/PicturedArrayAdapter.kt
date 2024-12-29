package dev.liinahamari.suggestions_ui

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.core.ext.layoutInflater
import dev.liinahamari.suggestions_ui.databinding.SuggestionsListItemBinding

class PicturedArrayAdapter(private val context: Context) :
    ArrayAdapter<SuggestionUi>(context, R.layout.suggestions_list_item) {
    init {
        setNotifyOnChange(true)
    }

    fun replaceAll(dataset: List<SuggestionUi>) {
        clear()
        addAll(dataset)
        filter.filter(null)
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View = convertView.getBinding(parent)
        .apply { getItem(position)!!.render(this) }
        .root

    private fun SuggestionUi.render(ui: SuggestionsListItemBinding) {
        ui.titleTv.text = title
        ui.titleTv.setSelected(true);

        ui.yearTv.text = year.toString()
        ui.genresTv.text = genres.joinToString()
        posterUrl?.let {
            Glide.with(context)
                .load(posterUrl)
//              .load(/*"https://image.tmdb.org/t/p/w154${*/getItem(position)?.posterUrl/*}"*/)
                .fallback(R.drawable.broken_image_24)
                .error(R.drawable.broken_image_24)
                .timeout(10_000)
                .override(100, 100)
                .listener(UnveilRequestListener { ui.veilLayout.unVeil() })
                .into(ui.thumbnail)
        }
    }

    private fun View?.getBinding(parent: ViewGroup): SuggestionsListItemBinding = if (this == null) {
        SuggestionsListItemBinding.inflate(context.layoutInflater, parent, false)
    } else {
        SuggestionsListItemBinding.bind(this)
    }
}

data class SuggestionUi(val title: String, val year: Int, val posterUrl: String?, val genres: List<String>)

fun List<Movie>.toMoviesUi() = map { it.toUi() }
fun List<Book>.toBooksUi() = map { it.toUi() }
fun List<Game>.toGamesUi() = map { it.toUi() }

private fun Movie.toUi() = SuggestionUi(
    title = title,
    year = releaseYear,
    genres = genres.map { it.name.replace("_", " ").lowercase() },
    posterUrl = posterUrl
)

private fun Book.toUi() = SuggestionUi(
    title = name,
    year = year,
    posterUrl = posterUrl,
    genres = genres.map { it.name.replace("_", " ").lowercase() }
)

private fun Game.toUi() = SuggestionUi(
    title = name,
    year = year,
    posterUrl = posterUrl,
    genres = genres.map { it.name.replace("_", " ").lowercase() }
)
