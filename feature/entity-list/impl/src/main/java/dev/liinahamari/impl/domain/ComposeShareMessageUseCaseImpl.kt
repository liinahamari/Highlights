package dev.liinahamari.impl.domain

import dev.liinahamari.api.domain.entities.BookUi
import dev.liinahamari.api.domain.entities.DocumentaryUi
import dev.liinahamari.api.domain.entities.GameUi
import dev.liinahamari.api.domain.entities.MovieUi
import dev.liinahamari.api.domain.entities.ShareMessage
import dev.liinahamari.api.domain.entities.ShortUi
import dev.liinahamari.api.domain.usecases.ComposeShareMessageUseCase

class ComposeShareMessageUseCaseImpl : ComposeShareMessageUseCase {
    override fun composeBooksShareMessage(entries: List<BookUi>): ShareMessage {
        val (shareTitle, shareContent) = if (entries.size == 1) {
            with(entries.first()) {
                title to description + System.lineSeparator() + System.lineSeparator() + (coverUrl ?: "")
            }
        } else {
            "" to entries.joinToString("\n") {
                (it.title + System.lineSeparator() +
                        (it.author ?: "") +
                        System.lineSeparator() +
                        it.description +
                        System.lineSeparator() +
                        System.lineSeparator() +
                        (it.coverUrl ?: "") +
                        System.lineSeparator() +
                        System.lineSeparator()
                        )
            }
        }
        return ShareMessage(shareTitle, shareContent)
    }

    override fun composeShortsShareMessage(entries: List<ShortUi>): ShareMessage {
        val (shareTitle, shareContent) = if (entries.size == 1) {
            with(entries.first()) {
                title to description + System.lineSeparator() + System.lineSeparator() + (posterUrl ?: "")
            }
        } else {
            "" to entries.joinToString("\n") {
                (it.title + System.lineSeparator() +
                        it.description +
                        System.lineSeparator() +
                        System.lineSeparator() +
                        (it.posterUrl ?: "") +
                        System.lineSeparator() +
                        System.lineSeparator()
                        )
            }
        }
        return ShareMessage(shareTitle, shareContent)
    }

    override fun composeMovieShareMessage(entries: List<MovieUi>): ShareMessage {
        val (shareTitle, shareContent) = if (entries.size == 1) {
            with(entries.first()) {
                title to description + System.lineSeparator() + System.lineSeparator() + (posterUrl ?: "")
            }
        } else {
            "" to entries.joinToString("\n") {
                (it.title + System.lineSeparator() +
                        it.description +
                        System.lineSeparator() +
                        System.lineSeparator() +
                        (it.posterUrl ?: "") +
                        System.lineSeparator() +
                        System.lineSeparator()
                        )
            }
        }
        return ShareMessage(shareTitle, shareContent)
    }

    override fun composeGameShareMessage(entries: List<GameUi>): ShareMessage {
        val (shareTitle, shareContent) = if (entries.size == 1) {
            with(entries.first()) {
                title to description + System.lineSeparator() + System.lineSeparator() + (posterUrl ?: "")
            }
        } else {
            "" to entries.joinToString("\n") {
                (it.title + System.lineSeparator() +
                        it.description +
                        System.lineSeparator() +
                        System.lineSeparator() +
                        (it.posterUrl ?: "") +
                        System.lineSeparator() +
                        System.lineSeparator()
                        )
            }
        }
        return ShareMessage(shareTitle, shareContent)
    }

    override fun composeDocumentariesShareMessage(entries: List<DocumentaryUi>): ShareMessage {
        val (shareTitle, shareContent) = if (entries.size == 1) {
            with(entries.first()) {
                title to description + System.lineSeparator() + System.lineSeparator() + (posterUrl ?: "")
            }
        } else {
            "" to entries.joinToString("\n") {
                (it.title + System.lineSeparator() +
                        it.description +
                        System.lineSeparator() +
                        System.lineSeparator() +
                        (it.posterUrl ?: "") +
                        System.lineSeparator() +
                        System.lineSeparator()
                        )
            }
        }
        return ShareMessage(shareTitle, shareContent)
    }
}
