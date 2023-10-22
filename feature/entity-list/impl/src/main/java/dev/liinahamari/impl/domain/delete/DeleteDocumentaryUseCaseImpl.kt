package dev.liinahamari.impl.domain.delete

import dev.liinahamari.api.domain.usecases.delete.DeleteDocumentaryUseCase
import dev.liinahamari.impl.data.repos.DocumentariesRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

internal class DeleteDocumentaryUseCaseImpl @Inject constructor(private val documentariesRepo: DocumentariesRepo) :
    DeleteDocumentaryUseCase {
    override fun deleteDocumentary(id: Long): Completable = documentariesRepo.delete(id)
}
