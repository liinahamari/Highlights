package dev.liinahamari.suggestions_ui.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.suggestions.MovieSuggestionsListFactory
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.GameGenre
import dev.liinahamari.core.SingleLiveEvent
import dev.liinahamari.suggestions.api.MovieSuggestionsDependencies
import dev.liinahamari.suggestions.api.model.toDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.UnknownHostException

class SearchGameViewModel(application: Application) : AndroidViewModel(application) {
    private val _searchGameEvent = SingleLiveEvent<GetRemoteGames>()
    val searchGameEvent: LiveData<GetRemoteGames> get() = _searchGameEvent

    private val api by lazy {
        MovieSuggestionsListFactory.getApi(object : MovieSuggestionsDependencies {
            override val application: Application
                get() = application
        })
    }

    private val searchGameUseCase by lazy { api.searchGameUseCase }

    fun searchGame(query: String, category: Category): Observable<GetRemoteGames> =
        searchGameUseCase.search(query)
            .flatMapObservable { Observable.fromIterable(it) }
            .filter {
                (it.name.isNullOrBlank() || it.name == "null").not()
                        && it.releasedDate.isNullOrBlank().not()
                        && it.id != null
            }
            .map {
                GameGenre.values()
                    .filter { localGenreName ->
                        val remoteGenres = it.genres?.map { it.genreName }?.filter { it.isNullOrBlank().not() } ?: emptyList()
                        remoteGenres.any { localGenreName.name.contains(it!!, true) }
                    }
                    .sorted() to it
            }
            .map { it.second.toDomain(category).copy(genres = it.first) }
            .toList()
            .map<GetRemoteGames>(GetRemoteGames::Success)
            .doOnError { it.printStackTrace() } //fixme reorder in other viewmodels
            .onErrorReturn {
                when (it) {
                    is UnknownHostException -> GetRemoteGames.Error.NoInternetError
                    else -> GetRemoteGames.Error.CommonError
                }
            }
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(_searchGameEvent::setValue)
}

sealed interface GetRemoteGames {
    data class Success(val games: List<Game>) : GetRemoteGames
    sealed interface Error : GetRemoteGames {
        object NoInternetError : Error
        object CommonError : Error
    }
}
