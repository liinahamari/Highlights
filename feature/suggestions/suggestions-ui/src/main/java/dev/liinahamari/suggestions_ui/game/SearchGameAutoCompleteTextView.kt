package dev.liinahamari.suggestions_ui.game

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.jakewharton.rxbinding4.widget.textChanges
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.core.ext.toast
import dev.liinahamari.suggestions_ui.PicturedArrayAdapter
import dev.liinahamari.suggestions_ui.SuggestionUi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SearchGameAutoCompleteTextView @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.autoCompleteTextViewStyle
) : MaterialAutoCompleteTextView(context, attributeSet, defStyleAttr) {
    private val viewModel by lazy { ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<SearchGameViewModel>() }
    private val suggestionsAdapter: PicturedArrayAdapter by lazy { PicturedArrayAdapter(context) }
    private val disposable = CompositeDisposable()
    var categoryArg: Category = Category.GOOD //fixme actual

    private lateinit var bookObserver: GameObserver

    fun setOnItemChosenListener(bo: GameObserver) {
        this.bookObserver = bo
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setAdapter(suggestionsAdapter)
        disposable.add(textChanges()
            .filter { it.isBlank().not() }
            .map { it.toString() }
            .throttleLast(1300, TimeUnit.MILLISECONDS)
            .switchMap(::searchGame)
            .subscribe())

        setupViewModelSubscriptions()
    }

    private fun searchGame(query: String): Observable<GetRemoteGames> = viewModel.searchGame(query, categoryArg)

    private fun setupViewModelSubscriptions() {
        viewModel.searchGameEvent.observe(findViewTreeLifecycleOwner()!!) {
            when (it) {
                is GetRemoteGames.Error.CommonError -> context.toast("Suggestions API failed")
                is GetRemoteGames.Error.NoInternetError -> context.toast("Check the Internet connection")

                is GetRemoteGames.Success -> {
                    suggestionsAdapter.replaceAll(it.games.map {
                        SuggestionUi(
                            title = it.name,
                            year = it.year,
                            posterUrl = it.posterUrl,
                            genres = it.genres.map { it.name.replace("_", " ").lowercase() }
                        )
                    }
                    )
                    setOnItemClickListener { _, _, position, _ -> bookObserver.onChosen(it.games[position]) }
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        disposable.dispose()
        super.onDetachedFromWindow()
    }

    interface GameObserver {
        fun onChosen(g: Game)
    }

    override fun convertSelectionToString(selectedItem: Any?): CharSequence = (selectedItem as SuggestionUi).title
}
