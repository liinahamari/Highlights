package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.entities.EntryUi
import dev.liinahamari.api.domain.entities.ShareMessage
import dev.liinahamari.api.domain.usecases.ComposeShareMessageUseCase

class ComposeShareMessageUseCaseImpl: ComposeShareMessageUseCase {
    override fun composeShareMessage(entries: List<EntryUi>): ShareMessage {
        val (shareTitle, shareContent) = if (entries.size == 1) {
            entries.first().title to with(entries.first()) {
                (description + System.lineSeparator() + url)
            }
        } else {
            "" to entries.joinToString("\n") {
                (it.title + System.lineSeparator() + it.description + System.lineSeparator() + it.url + System.lineSeparator() + System.lineSeparator())
            }
        }
        return ShareMessage(shareTitle, shareContent)
    }
}
