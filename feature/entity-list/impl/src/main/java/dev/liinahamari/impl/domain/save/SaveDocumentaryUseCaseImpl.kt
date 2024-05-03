package dev.liinahamari.impl.domain.save

import dev.liinahamari.api.domain.entities.Documentary
import dev.liinahamari.api.domain.usecases.save.SaveDocumentaryUseCase
import dev.liinahamari.impl.data.repos.DocumentariesRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

internal class SaveDocumentaryUseCaseImpl @Inject constructor(private val documentariesRepo: DocumentariesRepo) :
    SaveDocumentaryUseCase {
    override fun saveDocumentaries(vararg documentaries: Documentary): Completable = documentariesRepo.save(*documentaries)
}
