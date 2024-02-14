package dev.liinahamari.api.domain.entities

data class DatabaseCounters(
    val entities: List<Entity>,
    val totalCounter: String,
    val titleInCenterOfChart: String
)

sealed interface Entity {
    val counter: Float
    val label: String

    data class Games(override val counter: Float, override val label: String = Games::class.java.simpleName) : Entity
    data class Documentaries(
        override val counter: Float,
        override val label: String = Documentaries::class.java.simpleName
    ) : Entity

    data class Movies(override val counter: Float, override val label: String = Movies::class.java.simpleName) : Entity
    data class Books(override val counter: Float, override val label: String = Books::class.java.simpleName) : Entity
}
