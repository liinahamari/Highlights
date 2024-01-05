package dev.liinahamari.suggestions_ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.jakewharton.rxbinding4.widget.textChanges
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.core.ext.getParcelableOf
import dev.liinahamari.core.ext.toast
import dev.liinahamari.suggestions_ui.SearchMoviesViewModel.GetRemoteMovies
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

const val ARG_CATEGORY = "search_movie_category"

/** To work properly, this custom View requires Fragment-holder to hold argument of [ARG_CATEGORY]
 *  It should be one of enum's [dev.liinahamari.api.domain.entities.Category] values
 *  For example, while instantiating your fragment, apply arguments to it:
 *  YourFragment().apply { arguments = bundleOf(ARG_CATEGORY to category)
 * */
class SearchMovieAutoCompleteTextView @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.autoCompleteTextViewStyle
) : MaterialAutoCompleteTextView(context, attributeSet, defStyleAttr) {
    private val viewModel by lazy { ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<SearchMoviesViewModel>() }
    private val suggestionsAdapter: PicturedArrayAdapter by lazy { PicturedArrayAdapter(context) }
    private val disposable = CompositeDisposable()
    private val categoryArg: Category by lazy {
        findFragment<Fragment>().requireArguments().getParcelableOf(ARG_CATEGORY)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setAdapter(suggestionsAdapter)
        viewModel.fetchMovieGenres()
        disposable.add(textChanges()
            .filter { it.isBlank().not() }
            .map { it.toString() }
            .throttleLast(1300, TimeUnit.MILLISECONDS)
            .switchMap { viewModel.searchForMovie(it, categoryArg) }
            .subscribe())

        setupViewModelSubscriptions()
    }

    private fun setupViewModelSubscriptions() {
        viewModel.searchMoviesResultEvent.observe(findViewTreeLifecycleOwner()!!) {
            when (it) {
                is GetRemoteMovies.Error.CommonError -> context.toast("Suggestions API failed")
                is GetRemoteMovies.Error.NoInternetError -> context.toast("Check the Internet connection")

                is GetRemoteMovies.Success -> {
                    suggestionsAdapter.replaceAll(it.movies)
                    setOnItemClickListener { _, _, position, _ ->
                        RemoteMoviePreviewFragment.newInstance(it.movies[position], categoryArg)
                            .show(findFragment<Fragment>().childFragmentManager, "abc")
                    }
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        disposable.dispose()
        super.onDetachedFromWindow()
    }

    override fun replaceText(text: CharSequence?) = Unit
    override fun dismissDropDown() = Unit
}
