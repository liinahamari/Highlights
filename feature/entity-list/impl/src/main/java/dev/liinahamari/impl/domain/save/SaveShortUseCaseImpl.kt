package dev.liinahamari.impl.domain.save

import dev.liinahamari.api.domain.entities.Short
import dev.liinahamari.api.domain.usecases.save.SaveShortsUseCase
import dev.liinahamari.impl.data.repos.ShortsRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

internal class SaveShortUseCaseImpl @Inject constructor(private val shortsRepo: ShortsRepo) :
    SaveShortsUseCase {
    override fun saveShorts(vararg shorts: Short): Completable = shortsRepo.save(*shorts)
}
