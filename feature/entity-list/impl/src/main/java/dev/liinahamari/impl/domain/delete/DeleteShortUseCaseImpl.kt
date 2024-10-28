package dev.liinahamari.impl.domain.delete

import dev.liinahamari.api.domain.usecases.delete.DeleteShortUseCase
import dev.liinahamari.impl.data.repos.ShortsRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteShortUseCaseImpl @Inject constructor(private val shortsRepo: ShortsRepo) : DeleteShortUseCase {
    override fun deleteShort(id: Long): Completable = shortsRepo.delete(id)
}
