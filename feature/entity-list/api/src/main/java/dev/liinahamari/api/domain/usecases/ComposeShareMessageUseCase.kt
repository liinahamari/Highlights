package dev.liinahamari.api.domain.usecases

import dev.liinahamari.api.domain.entities.EntryUi
import dev.liinahamari.api.domain.entities.ShareMessage

interface ComposeShareMessageUseCase {
    fun composeShareMessage(entries: List<EntryUi>): ShareMessage
}
