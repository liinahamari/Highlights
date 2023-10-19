package dev.liinahamari.api.domain.entities

data class Game(
    val id: Long,
    val category: Category,
    val year: Int,
    val posterUrl: String,
    val countryCodes: List<String>,
    val name: String,
    val genres: List<GameGenre>,
)

enum class GameGenre {
    ADVENTURE,
    FIGHTING,
    FPS,
    RPG,
    SIMULATION,
    RTS,
    TPS,
    STRATEGY,
    SURVIVAL_HORROR
}
