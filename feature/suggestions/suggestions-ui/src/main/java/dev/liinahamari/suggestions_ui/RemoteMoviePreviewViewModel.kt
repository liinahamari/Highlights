package dev.liinahamari.suggestions_ui

import androidx.lifecycle.ViewModel
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.suggestions.api.model.RemoteMovie

class RemoteMoviePreviewViewModel : ViewModel() {
    fun saveMovie(movie: RemoteMovie, category: Category): Nothing = TODO("Not yet implemented")
}
