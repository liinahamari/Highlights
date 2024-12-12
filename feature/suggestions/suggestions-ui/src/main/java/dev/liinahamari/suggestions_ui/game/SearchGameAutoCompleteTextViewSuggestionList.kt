package dev.liinahamari.suggestions_ui.game

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding4.widget.textChanges
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.core.ext.toast
import dev.liinahamari.core.views.HideSuggestionListOnScrollMaterialAutoCompleteTextView
import dev.liinahamari.suggestions_ui.PicturedArrayAdapter
import dev.liinahamari.suggestions_ui.SuggestionUi
import dev.liinahamari.suggestions_ui.startCircularProgress
import dev.liinahamari.suggestions_ui.toGamesUi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SearchGameAutoCompleteTextView @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.autoCompleteTextViewStyle
) : HideSuggestionListOnScrollMaterialAutoCompleteTextView(context, attributeSet, defStyleAttr) {
    private val viewModel by lazy { ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<SearchGameViewModel>() }
    private val suggestionsAdapter: PicturedArrayAdapter by lazy { PicturedArrayAdapter(context) }
    private val disposable = CompositeDisposable()
    private var suggestionsEnabled = true
    var categoryArg: Category = Category.GOOD //fixme actual
    private val textInput = parent.parent as TextInputLayout

    private lateinit var bookObserver: GameObserver

    fun setOnItemChosenListener(bo: GameObserver) {
        this.bookObserver = bo
    }

    fun setSuggestionsEnabled(suggestionsEnabled: Boolean) {
        this.suggestionsEnabled = suggestionsEnabled
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (suggestionsEnabled) {
            setPaddingRelative(paddingStart, 0, paddingEnd+compoundPaddingEnd, 0)
            setAdapter(suggestionsAdapter)
            disposable.add(textChanges()
                .filter { it.isBlank().not() }
                .map { it.toString() }
                .throttleLast(1300, TimeUnit.MILLISECONDS)
                .switchMap(::searchGame)
                .subscribe())

            setupViewModelSubscriptions()
        } else {
            setAdapter(null)
        }
    }

    private fun searchGame(query: String): Observable<GetRemoteGames> = viewModel.searchGame(query, categoryArg)

    private fun setupViewModelSubscriptions() {
        viewModel.searchGameEvent.observe(findViewTreeLifecycleOwner()!!) {
            when (it) {
                is GetRemoteGames.Loading -> {
                    textInput.isEndIconVisible = false
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        context.applicationContext.startCircularProgress(),
                        null
                    )
                }

                is GetRemoteGames.Error.CommonError -> {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                    textInput.isEndIconVisible = true
                    context.toast("Suggestions API failed")
                }

                is GetRemoteGames.Error.NoInternetError -> {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                    textInput.isEndIconVisible = true
                    context.toast("Check the Internet connection")
                }

                is GetRemoteGames.Success -> {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                    textInput.isEndIconVisible = true
                    suggestionsAdapter.replaceAll(it.games.toGamesUi())
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
