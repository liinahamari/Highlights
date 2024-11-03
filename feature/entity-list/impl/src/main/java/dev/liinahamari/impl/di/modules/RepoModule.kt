package dev.liinahamari.impl.di.modules

import dagger.Binds
import dagger.Module
import dev.liinahamari.api.domain.repo.ShakeCounterRepo
import dev.liinahamari.impl.data.repos.BooksRepo
import dev.liinahamari.impl.data.repos.BooksRepoImpl
import dev.liinahamari.impl.data.repos.DocumentariesRepo
import dev.liinahamari.impl.data.repos.DocumentariesRepoImpl
import dev.liinahamari.impl.data.repos.GamesRepo
import dev.liinahamari.impl.data.repos.GamesRepoImpl
import dev.liinahamari.impl.data.repos.MoviesRepo
import dev.liinahamari.impl.data.repos.MoviesRepoImpl
import dev.liinahamari.impl.data.repos.ShakeCounterRepoImpl
import dev.liinahamari.impl.data.repos.ShortsRepo
import dev.liinahamari.impl.data.repos.ShortsRepoImpl
import javax.inject.Singleton

@Module
interface RepoModule {
    @Binds
    @Singleton
    fun moviesRepo(impl: MoviesRepoImpl): MoviesRepo

    @Binds
    @Singleton
    fun gamesRepo(impl: GamesRepoImpl): GamesRepo

    @Binds
    @Singleton
    fun shortsRepo(impl: ShortsRepoImpl): ShortsRepo

    @Binds
    @Singleton
    fun documentariesRepo(impl: DocumentariesRepoImpl): DocumentariesRepo

    @Binds
    @Singleton
    fun booksRepo(impl: BooksRepoImpl): BooksRepo

    @Binds
    fun shakeCounterRepo(impl: ShakeCounterRepoImpl): ShakeCounterRepo
}
