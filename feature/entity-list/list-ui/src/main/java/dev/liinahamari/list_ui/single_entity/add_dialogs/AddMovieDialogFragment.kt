package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.list_ui.MainActivity
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.FragmentAddEntryBinding
import dev.liinahamari.list_ui.viewmodels.SaveEntryViewModel
import dev.liinahamari.list_ui.viewmodels.SaveEvent
import dev.liinahamari.suggestions_ui.ARG_CATEGORY
import dev.liinahamari.suggestions_ui.SearchMovieAutoCompleteTextView
import javax.inject.Inject


//fixme leak?
class AddMovieDialogFragment : DialogFragment(R.layout.fragment_add_entry) {
    private lateinit var ui: FragmentAddEntryBinding

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }

    private var movie = Movie.default()

    companion object {
        fun newInstance(category: Category): AddMovieDialogFragment = AddMovieDialogFragment().apply {
            arguments = bundleOf(ARG_CATEGORY to category)
        }
    }

    override fun onAttach(context: Context) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
        .setView((FragmentAddEntryBinding.inflate(layoutInflater)).also { ui = it }.root)
        .setPositiveButton(R.string.save) { _, _ -> saveEntryViewModel.saveMovie(movie) }
        .create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupViewModelSubscriptions()
    }

    private fun setupViewModelSubscriptions() = saveEntryViewModel.saveEvent.observe(this) {
        when (it) {
            is SaveEvent.Failure -> toast("Failed to save movie")
            is SaveEvent.Success -> dismiss()
        }
    }

    private fun setupUi() {
        ui.countrySelectionBtn.setOnClickListener {
            requireContext().showCountrySelectionDialog { movie = movie.copy(countryCodes = it) }
        }
        ui.genreBtn.setOnClickListener {
            requireContext().showMovieGenreSelectionDialog { movie = movie.copy(genres = it) }
        }

        ui.nameEt.categoryArg = requireArguments().getParcelableOf(ARG_CATEGORY)
        ui.nameEt.setOnItemChosenListener(object : SearchMovieAutoCompleteTextView.MovieObserver {
            override fun onChosen(mov: Movie) {
                ui.yearEt.setText(mov.year.toString())
                ui.posterUrlEt.setText(mov.posterUrl)

                movie = mov
            }
        })
    }
}
