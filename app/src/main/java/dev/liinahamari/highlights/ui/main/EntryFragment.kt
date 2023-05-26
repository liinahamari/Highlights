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
import dev.liinahamari.highlights.ui.main.CategoriesFragment.Companion.ARG_CATEGORY

class EntryFragment : Fragment(R.layout.fragment_category) {
    private var movie = Movie("", "", 0, EntityCategory.GOOD, "")
    private var documentary = Documentary("", 0, EntityCategory.GOOD, "")
    private var game = Game("", "", 0, EntityCategory.GOOD, "")
    private var book = Book("", "", "", 0, EntityCategory.GOOD, "")

    private val entityType by lazy { requireArguments().getString(ARG_ENTITY_TYPE) }
    private val category: EntityCategory by lazy { EntityCategory.valueOf(requireArguments().getString(ARG_CATEGORY)!!) }
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
                            when (entityType) {
                                "bookDao" -> book = book.copy(name = text.toString())
                                "movieDao" -> movie = movie.copy(name = text.toString())
                                "documentaryDao" -> documentary = documentary.copy(name = text.toString())
                                "gameDao" -> game = game.copy(name = text.toString())
                                else -> throw IllegalStateException()
                            }
                        }
                    })
                    if (entityType != "documentaryDao") {
                        addView(EditText(requireContext()).apply {
                            setHint(R.string.genre)
                            doOnTextChanged { text, _, _, _ ->
                                when (entityType) {
                                    "bookDao" -> book = book.copy(genre = text.toString())
                                    "movieDao" -> movie = movie.copy(genre = text.toString())
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
                            when (entityType) {
                                "bookDao" -> book = book.copy(year = text.toString().toInt())
                                "movieDao" -> movie = movie.copy(year = text.toString().toInt())
                                "documentaryDao" -> documentary = documentary.copy(year = text.toString().toInt())
                                "gameDao" -> game = game.copy(year = text.toString().toInt())
                                else -> throw IllegalStateException()
                            }
                        }
                    })
                    addView(EditText(requireContext()).apply {
                        inputType = InputType.TYPE_CLASS_TEXT
                        setHint(R.string.poster_url)
                        doOnTextChanged { text, _, _, _ ->
                            when (entityType) {
                                "bookDao" -> book = book.copy(posterUrl = text.toString())
                                "movieDao" -> movie = movie.copy(posterUrl = text.toString())
                                "documentaryDao" -> documentary = documentary.copy(posterUrl = text.toString())
                                "gameDao" -> game = game.copy(posterUrl = text.toString())
                                else -> throw IllegalStateException()
                            }
                        }
                    })
                    if (entityType == "bookDao") {
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
                    when (entityType) {
                        "bookDao" -> db.bookDao().insert(book.copy(category = category))
                        "movieDao" -> db.movieDao().insert(movie.copy(category = category))
                        "documentaryDao" -> db.documentaryDao().insert(documentary.copy(category = category))
                        "gameDao" -> db.gameDao().insert(game.copy(category = category))
                        else -> throw IllegalStateException()
                    }
                    val data: List<Entry> = when (entityType) {
                        "bookDao" -> db.bookDao().getAll()
                            .map { Entry(description = "${it.author}: ${it.name}", url = it.posterUrl) }
                        "movieDao" -> db.movieDao().getAll().map { Entry(description = it.name, url = it.posterUrl) }
                        "documentaryDao" -> db.documentaryDao().getAll()
                            .map { Entry(description = it.name, url = it.posterUrl) }
                        "gameDao" -> db.gameDao().getAll().map { Entry(description = it.name, url = it.posterUrl) }
                        else -> throw IllegalStateException()
                    }
                    ui.entriesRv.adapter = EntryAdapter(data).apply { notifyDataSetChanged() }
                }
            }.show()
        }

        val data: List<Entry> = when (entityType) {
            "bookDao" -> db.bookDao().getAll()
                .map { Entry(description = "${it.author}: ${it.name}", url = it.posterUrl) }
            "movieDao" -> db.movieDao().getAll().map { Entry(description = it.name, url = it.posterUrl) }
            "documentaryDao" -> db.documentaryDao().getAll().map { Entry(description = it.name, url = it.posterUrl) }
            "gameDao" -> db.gameDao().getAll().map { Entry(description = it.name, url = it.posterUrl) }
            else -> throw IllegalStateException()
        }

        ui.entriesRv.adapter = EntryAdapter(data).apply { notifyDataSetChanged() }
    }

    companion object {
        const val ARG_ENTITY_TYPE = "arg_entity_type"
        @JvmStatic fun newInstance(entityType: String, category: String) =
            EntryFragment().apply {
                arguments = bundleOf(ARG_ENTITY_TYPE to entityType, ARG_CATEGORY to category)
            }
    }
}
