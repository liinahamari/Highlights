package dev.liinahamari.highlights.ui.main

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.liinahamari.highlights.App
import dev.liinahamari.highlights.R
import dev.liinahamari.highlights.databinding.FragmentCategoryBinding
import dev.liinahamari.highlights.db.daos.Book
import dev.liinahamari.highlights.db.daos.Documentary
import dev.liinahamari.highlights.db.daos.Game
import dev.liinahamari.highlights.db.daos.Movie

class CategoryFragment : Fragment(R.layout.fragment_category) {
    private var movie = Movie("", "", 0)
    private var documentary = Documentary("", 0)
    private var game = Game("", "", 0)
    private var book = Book("", "", "", 0)

    private val category by lazy { requireArguments().getString(ARG_CATEGORY) }
    private val db by lazy { (requireActivity().application as App).db }
    private val ui by viewBinding(FragmentCategoryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ui.entriesRv.layoutManager = LinearLayoutManager(requireContext())

        ui.fab.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(requireContext()).apply {
                setView(LinearLayout(requireContext()).apply {
                    orientation = VERTICAL
                    addView(EditText(requireContext()).apply {
                        setHint(R.string.name)
                        doOnTextChanged { text, _, _, _ ->
                            when (category) {
                                "bookDao" -> book = book.copy(name = text.toString())
                                "movieDao" -> movie = movie.copy(name = text.toString())
                                "documentaryDao" -> documentary = documentary.copy(name = text.toString())
                                "gameDao" -> game = game.copy(name = text.toString())
                                else -> throw IllegalStateException()
                            }
                        }
                    })
                    if (category != "documentaryDao") {
                        addView(EditText(requireContext()).apply {
                            setHint(R.string.genre)
                            doOnTextChanged { text, _, _, _ ->
                                when (category) {
                                    "bookDao" -> book = book.copy(genre = text.toString())
                                    "moviesDao" -> movie = movie.copy(genre = text.toString())
                                    "gameDao" -> game = game.copy(genre = text.toString())
                                    else -> throw IllegalStateException()
                                }
                            }
                        })
                    }
                    addView(EditText(requireContext()).apply {
                        inputType = InputType.TYPE_CLASS_NUMBER
                        setHint(R.string.year)
                        doOnTextChanged { text, _, _, _ ->
                            when (category) {
                                "bookDao" -> book = book.copy(year = text.toString().toInt())
                                "moviesDao" -> movie = movie.copy(year = text.toString().toInt())
                                "documentaryDao" -> documentary = documentary.copy(year = text.toString().toInt())
                                "gameDao" -> game = game.copy(year = text.toString().toInt())
                                else -> throw IllegalStateException()
                            }
                        }
                    })
                    if (category == "bookDao") {
                        addView(EditText(requireContext()).apply {
                            inputType = InputType.TYPE_CLASS_TEXT
                            setHint(R.string.author)
                            doOnTextChanged { text, _, _, _ ->
                                book = book.copy(author = text.toString())
                            }
                        })
                    }
                })
                setPositiveButton(android.R.string.ok) { _, _ ->
                    when (category) {
                        "bookDao" -> db.bookDao().insert(book)
                        "moviesDao" -> db.movieDao().insert(movie)
                        "documentaryDao" -> db.documentaryDao().insert(documentary)
                        "gameDao" -> db.gameDao().insert(game)
                        else -> throw IllegalStateException()
                    }
                    val data: List<String> = when (category) {
                        "bookDao" -> db.bookDao().getAll().map { it.toString() }
                        "moviesDao" -> db.movieDao().getAll().map { it.toString() }
                        "documentaryDao" -> db.documentaryDao().getAll().map { it.toString() }
                        "gameDao" -> db.gameDao().getAll().map { it.toString() }
                        else -> throw IllegalStateException()
                    }
                    ui.entriesRv.adapter = EntryAdapter(data).apply { notifyDataSetChanged() }
                }
            }.show()
        }

        val data: List<String> = when (category) {
            "bookDao" -> db.bookDao().getAll().map { it.toString() }
            "moviesDao" -> db.movieDao().getAll().map { it.toString() }
            "documentaryDao" -> db.documentaryDao().getAll().map { it.toString() }
            "gameDao" -> db.gameDao().getAll().map { it.toString() }
            else -> throw IllegalStateException()
        }

        ui.entriesRv.adapter = EntryAdapter(data).apply { notifyDataSetChanged() }
    }

    companion object {
        const val ARG_CATEGORY = "arg_category"
        @JvmStatic fun newInstance(categoryName: String) =
            CategoryFragment().apply { arguments = bundleOf(ARG_CATEGORY to categoryName) }
    }
}
