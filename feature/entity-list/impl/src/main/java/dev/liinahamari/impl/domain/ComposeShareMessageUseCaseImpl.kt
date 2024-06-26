package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.entities.EntryUi
import dev.liinahamari.api.domain.entities.ShareMessage
import dev.liinahamari.api.domain.usecases.ComposeShareMessageUseCase

class ComposeShareMessageUseCaseImpl : ComposeShareMessageUseCase {
    override fun composeShareMessage(entries: List<EntryUi>): ShareMessage {
        val (shareTitle, shareContent) = if (entries.size == 1) {
            with(entries.first()) {
                title to description + System.lineSeparator() + System.lineSeparator() + (tmdbUrl ?: "")
            }
        } else {
            "" to entries.joinToString("\n") {
                (it.title + System.lineSeparator() +
                        it.description +
                        System.lineSeparator() +
                        System.lineSeparator() +
                        (it.tmdbUrl ?: "") +
                        System.lineSeparator() +
                        System.lineSeparator()
                        )
            }
        }
        return ShareMessage(shareTitle, shareContent)
    }
}
