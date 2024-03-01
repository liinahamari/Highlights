package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.GameGenre
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.activities.MainActivity
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentAddGameBinding
import dev.liinahamari.list_ui.viewmodels.SaveEntryViewModel
import dev.liinahamari.list_ui.viewmodels.SaveEvent
import dev.liinahamari.suggestions_ui.game.SearchGameAutoCompleteTextView
import dev.liinahamari.suggestions_ui.movie.ARG_CATEGORY
import javax.inject.Inject

class AddGameDialogFragment : DialogFragment(R.layout.fragment_add_game) {
    private var _ui: FragmentAddGameBinding? = null
    private val ui: FragmentAddGameBinding by lazy { _ui!! }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
        .setView((FragmentAddGameBinding.inflate(layoutInflater)).also { _ui = it }.root)
        .setNeutralButton(R.string.internet_search) { _, _ -> }
        .setPositiveButton(R.string.save) { _, _ -> saveEntryViewModel.saveGame(game) }
        .create()
        .apply {
            setOnShowListener {
                (dialog as AlertDialog).getButton(DialogInterface.BUTTON_NEUTRAL)
                    .setOnClickListener {
                        startActivity(
                            Intent(Intent.ACTION_WEB_SEARCH)
                                .putExtra(
                                    SearchManager.QUERY,
                                    ui.titleEt.text.toString() + " video game (${ui.yearEt.text})"
                                )
                        )
                    }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupViewModelSubscriptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    private fun setupViewModelSubscriptions() = saveEntryViewModel.saveEvent.observe(this) {
        when (it) {
            is SaveEvent.Failure -> toast("Failed to save documentary")
            is SaveEvent.Success -> requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun setupUi() {
        var selectedCountries = listOf<String>()
        var selectedGenres = listOf<GameGenre>()

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
}
