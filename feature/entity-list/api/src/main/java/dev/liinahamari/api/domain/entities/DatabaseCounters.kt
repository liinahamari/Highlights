package dev.liinahamari.api.domain.entities

sealed interface DatabaseCounters {
    data class Success(
        val entities: List<Entity>,
        val totalCounter: String,
        val titleInCenterOfChart: String
    ) : DatabaseCounters

    object Empty : DatabaseCounters
    sealed interface Error : DatabaseCounters
    object DatabaseCorruptionError : Error
}

sealed interface Entity {
    val counter: Int
    val label: String

    data class Games(override val counter: Int, override val label: String = Games::class.java.simpleName) : Entity
    data class Documentaries(
        override val counter: Int,
        override val label: String = Documentaries::class.java.simpleName
    ) : Entity
    data class Shorts(
        override val counter: Int,
        override val label: String = Short::class.java.simpleName
    ) : Entity

    data class Movies(override val counter: Int, override val label: String = Movies::class.java.simpleName) : Entity
    data class Books(override val counter: Int, override val label: String = Books::class.java.simpleName) : Entity
}
