package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
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
//todo toAlertDialog()?
class AddMovieDialogFragment : DialogFragment(R.layout.fragment_add_entry) {
    private val ui: FragmentAddEntryBinding by viewBinding(FragmentAddEntryBinding::bind)

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val saveEntryViewModel: SaveEntryViewModel by activityViewModels { viewModelFactory }
    private val argumentEntityCategory: Category by lazy { requireArguments().getParcelableOf(ARG_CATEGORY) }

    private var movie = Movie.default()

    companion object {
        fun newInstance(category: Category): AddMovieDialogFragment = AddMovieDialogFragment().apply {
            arguments = bundleOf(ARG_CATEGORY to category)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).listUiComponent.inject(this)
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupViewModelSubscriptions()
    }

    private fun setupViewModelSubscriptions() {
        saveEntryViewModel.saveEvent.observe(this) {
            when (it) {
                is SaveEvent.Failure -> toast("Failed to save movie")
                is SaveEvent.Success -> dismiss()
            }
        }
    }

    private fun setupUi() {
        ui.saveBtn.setOnClickListener { saveEntryViewModel.saveMovie(movie.copy(category = argumentEntityCategory)) }
        ui.countrySelectionBtn.setOnClickListener {
            requireContext().showCountrySelectionDialog { movie = movie.copy(countryCodes = it) }
        }
        ui.genreBtn.setOnClickListener {
            requireContext().showMovieGenreSelectionDialog { movie = movie.copy(genres = it) }
        }

        //todo to URL validating custom View
        ui.posterUrlEt.setOnFocusChangeListener { _, hasFocus: Boolean ->
            if (ui.posterUrlEt.text.isNullOrEmpty() || hasFocus) return@setOnFocusChangeListener
            if (URLUtil.isNetworkUrl(ui.posterUrlEt.text.toString())) {
                ui.posterUrlInputLayout.error = null
            } else {
                ui.posterUrlInputLayout.error = getString(R.string.url_is_not_valid)
            }
        }

        ui.nameEt.setOnItemChosenListener(object : SearchMovieAutoCompleteTextView.MovieObserver {
            override fun onChosen(mov: Movie) {
                ui.yearEt.setText(mov.year.toString())
                ui.posterUrlEt.setText(mov.posterUrl)

                movie = mov
            }
        })
    }
}
