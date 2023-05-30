package dev.liinahamari.highlights.ui.main

import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.db.daos.Book
import dev.liinahamari.highlights.db.daos.Documentary
import dev.liinahamari.highlights.db.daos.Game
import dev.liinahamari.highlights.db.daos.Movie

fun Fragment.showAddMovieDialog(category: EntityCategory, onSubmit: (movie: Movie) -> Unit) {
    var movie = Movie("", "", 0, category, "")

    AlertDialog.Builder(requireContext()).apply {
        setView(R.layout.add_entry_dialog)
        create()
        setPositiveButton(android.R.string.ok) { _, _ -> onSubmit.invoke(movie) }
    }.show().apply {
        requireView().findViewById<TextInputEditText>(R.id.nameTv).doOnTextChanged { text, _, _, _ ->
            movie = movie.copy(name = text.toString())
        }
        requireView().findViewById<TextInputEditText>(R.id.genreTv).doOnTextChanged { text, _, _, _ ->
            movie = movie.copy(genre = text.toString())
        }
        requireView().findViewById<TextInputEditText>(R.id.yearTv).doOnTextChanged { text, _, _, _ ->
            movie = movie.copy(year = text.toString().toInt())
        }
        requireView().findViewById<TextInputEditText>(R.id.posterUrlTv).doOnTextChanged { text, _, _, _ ->
            movie = movie.copy(posterUrl = text.toString())
        }
    }
}

fun Fragment.showAddDocumentaryDialog(category: EntityCategory, onSubmit: (documentary: Documentary) -> Unit) {
    var documentary = Documentary("", 0, category, "")
    AlertDialog.Builder(requireContext()).apply {
        setView(R.layout.add_entry_dialog)
        setPositiveButton(android.R.string.ok) { _, _ -> onSubmit.invoke(documentary) }
        create()
    }.show().apply {
        requireView().findViewById<TextInputEditText>(R.id.nameTv).doOnTextChanged { text, _, _, _ ->
            documentary = documentary.copy(name = text.toString())
        }
        requireView().findViewById<TextInputEditText>(R.id.genreTv).isGone = true
        requireView().findViewById<TextInputEditText>(R.id.yearTv).doOnTextChanged { text, _, _, _ ->
            documentary = documentary.copy(year = text.toString().toInt())
        }
        requireView().findViewById<TextInputEditText>(R.id.posterUrlTv).doOnTextChanged { text, _, _, _ ->
            documentary = documentary.copy(posterUrl = text.toString())
        }
    }
}

fun Fragment.showAddGameDialog(category: EntityCategory, onSubmit: (game: Game) -> Unit) {
    var game = Game("", "", 0, category, "")
    AlertDialog.Builder(requireContext()).apply {
        setView(R.layout.add_entry_dialog)
        setPositiveButton(android.R.string.ok) { _, _ -> onSubmit.invoke(game) }
        create()
    }.show().apply {
        requireView().findViewById<TextInputEditText>(R.id.nameTv).doOnTextChanged { text, _, _, _ ->
            game = game.copy(name = text.toString())
        }
        requireView().findViewById<TextInputEditText>(R.id.genreTv).doOnTextChanged { text, _, _, _ ->
            game = game.copy(genre = text.toString())
        }
        requireView().findViewById<TextInputEditText>(R.id.yearTv).doOnTextChanged { text, _, _, _ ->
            game = game.copy(year = text.toString().toInt())
        }
        requireView().findViewById<TextInputEditText>(R.id.posterUrlTv).doOnTextChanged { text, _, _, _ ->
            game = game.copy(posterUrl = text.toString())
        }
    }
}

fun Fragment.showAddBookDialog(category: EntityCategory, onSubmit: (book: Book) -> Unit) {
    var book = Book("", "", "", 0, category, "")
    AlertDialog.Builder(requireContext()).apply {
        setView(R.layout.add_entry_dialog)
        setPositiveButton(android.R.string.ok) { _, _ -> onSubmit.invoke(book) }
        create()
    }.show().apply {
        findViewById<TextInputEditText>(R.id.nameTv)?.doOnTextChanged { text, _, _, _ ->
            book = book.copy(name = text.toString())
        }
        findViewById<TextInputEditText>(R.id.authorTv)?.doOnTextChanged { text, _, _, _ ->
            book = book.copy(author = text.toString())
        }
        findViewById<TextInputEditText>(R.id.genreTv)?.doOnTextChanged { text, _, _, _ ->
            book = book.copy(genre = text.toString())
        }
        findViewById<TextInputEditText>(R.id.yearTv)?.doOnTextChanged { text, _, _, _ ->
            book = book.copy(year = text.toString().toInt())
        }
        findViewById<TextInputEditText>(R.id.posterUrlTv)?.doOnTextChanged { text, _, _, _ ->
            book = book.copy(posterUrl = text.toString())
        }
    }
}
