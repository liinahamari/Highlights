package dev.liinahamari.api.domain.usecases

import dev.liinahamari.api.domain.entities.BookUi
import dev.liinahamari.api.domain.entities.DocumentaryUi
import dev.liinahamari.api.domain.entities.GameUi
import dev.liinahamari.api.domain.entities.MovieUi
import dev.liinahamari.api.domain.entities.ShareMessage
import dev.liinahamari.api.domain.entities.ShortUi

interface ComposeShareMessageUseCase {
    fun composeBooksShareMessage(entries: List<BookUi>): ShareMessage
    fun composeShortsShareMessage(entries: List<ShortUi>): ShareMessage
    fun composeMovieShareMessage(entries: List<MovieUi>): ShareMessage
    fun composeGameShareMessage(entries: List<GameUi>): ShareMessage
    fun composeDocumentariesShareMessage(entries: List<DocumentaryUi>): ShareMessage
}
