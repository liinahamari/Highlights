package dev.liinahamari.sample

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.liinahamari.api.EntityListDependencies
import dev.liinahamari.api.domain.entities.Book
import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.Movie
import dev.liinahamari.entity_list.EntityListFactory
import dev.liinahamari.summary.sample.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        EntityListFactory.getApi(object :
            EntityListDependencies {
            override val application: Application
                get() = this@MainActivity.application
        })
            .apply {
                saveMovieUseCase.saveMovies(Movie.default()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
                saveBookUseCase.saveBooks(Book.default()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
                saveDocumentaryUseCase.saveDocumentaries(Documentary.default()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
                saveGameUseCase.saveGames(Game.default()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
            }
        Thread.sleep(1000)
        super.onCreate(savedInstanceState)
    }
}
