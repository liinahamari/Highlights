package dev.liinahamari.suggestions_ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.suggestions.api.model.RemoteMovie
import dev.liinahamari.suggestions_ui.databinding.FragmentMoviePreviewBinding

class RemoteMoviePreviewFragment : DialogFragment(R.layout.fragment_movie_preview) {
    private val ui by viewBinding(FragmentMoviePreviewBinding::bind)
    private val movieUi by lazy { requireArguments().getParcelableOf<RemoteMovie>(ARG_MOVIE) }
    private val category by lazy { requireArguments().getParcelableOf<Category>(ARG_CATEGORY) }
    private val moviePreviewViewModel: RemoteMoviePreviewViewModel by viewModels()

    companion object {
        private const val ARG_MOVIE = "arg_mov"
        private const val ARG_CATEGORY = "arg_category"
        fun newInstance(movie: RemoteMovie, category: Category) =
            RemoteMoviePreviewFragment().apply { arguments = bundleOf(ARG_MOVIE to movie, ARG_CATEGORY to category) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(ui) {
        renderMovieInformation()
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() = with(ui) {
        importBtn.setOnClickListener { moviePreviewViewModel.saveMovie(movieUi, category) } // todo make state "pressed"
        cancelBtn.setOnClickListener { dismiss() }
    }

    private fun renderMovieInformation() = with(ui) {
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500${movieUi.posterPath}")
            .timeout(5_000)
            .into(ui.posterIv)

        titleTv.text = movieUi.title
        yearTv.text = movieUi.releaseDate
        genresTv.text = movieUi.genres.toString()
        countriesTv.text = movieUi.originCountry.toString()
    }
}
