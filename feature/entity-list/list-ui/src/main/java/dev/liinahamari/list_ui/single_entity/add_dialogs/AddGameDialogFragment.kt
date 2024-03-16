package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.content.Context
import android.content.DialogInterface.OnClickListener
import androidx.core.os.bundleOf
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.GameGenre
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.databinding.FragmentAddGameBinding
import dev.liinahamari.suggestions_ui.game.SearchGameAutoCompleteTextView
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY

class AddGameDialogFragment : GenericAddFragment(R.layout.fragment_add_game) {
    private var _ui: FragmentAddGameBinding? = null
    private val ui: FragmentAddGameBinding by lazy { _ui!! }

    private var selectedCountries = listOf<String>()
    private var selectedGenres = listOf<GameGenre>()

    private var game = Game.default()

    companion object {
        fun newInstance(category: Category): AddGameDialogFragment = AddGameDialogFragment().apply {
            arguments = bundleOf(ARG_CATEGORY to category)
        }
    }

    override fun onAttach(context: Context) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        super.onAttach(context)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    override fun setupUi() {
        setupSelectionDialogs()
        setupTitleEditText()
    }

    private fun setupTitleEditText() {
        ui.titleEt.isSuggestionsEnabled = preferenceRepo.suggestionsEnabled
        ui.titleEt.categoryArg = requireArguments().getParcelableOf(ARG_CATEGORY)
        ui.titleEt.setOnItemChosenListener(object : SearchGameAutoCompleteTextView.GameObserver {
            override fun onChosen(g: Game) {
                ui.yearEt.setText(g.year.toString())
                ui.posterUrlEt.setText(g.posterUrl)

                game = Game(
                    id = 0L,
                    category = g.category,
                    year = g.year,
                    posterUrl = g.posterUrl,
                    countryCodes = g.countryCodes,
                    name = g.name,
                    description = g.description,
                    genres = g.genres
                )
                selectedCountries = g.countryCodes
                ui.countrySelectionBtn.text = g.countryCodes.toString()
                selectedGenres = g.genres
                ui.genreBtn.text = g.genres.toString()
            }
        })
    }

    private fun setupSelectionDialogs() {
        ui.countrySelectionBtn.setOnClickListener {
            showCountrySelectionDialog(selectedCountries) {
                selectedCountries = it
                game = game.copy(countryCodes = it)
                ui.countrySelectionBtn.text = it.toString()
            }
        }
        ui.genreBtn.setOnClickListener {
            showGameGenreSelectionDialog(selectedGenres) {
                selectedGenres = it
                game = game.copy(genres = it)
                ui.genreBtn.text = it.toString()
            }
        }
    }

    override fun getDialogCustomView() = (FragmentAddGameBinding.inflate(layoutInflater)).also { _ui = it }.root
    override fun onSaveButtonClicked(): OnClickListener = OnClickListener { _, _ -> saveEntryViewModel.saveGame(game) }
    override fun webSearchQuery(): String = ui.titleEt.text.toString() + " video game (${ui.yearEt.text})"
}
