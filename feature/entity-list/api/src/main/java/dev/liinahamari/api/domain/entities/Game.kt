package dev.liinahamari.api.domain.entities

data class Game(
    val id: Long = 0L,
    val category: Category,
    val year: Int,
    val posterUrl: String,
    val countryCodes: List<String>,
    val name: String,
    val genres: List<GameGenre>,
    val description: String
){
    companion object{
        fun default(category: Category= Category.GOOD) = Game(
            0L, category, 0, "", listOf(), "", listOf(), ""
        )
    }
}


enum class GameGenre {
    ADVENTURE,
    FIGHTING,
    FPS,
    RPG,
    SIMULATION,
    RTS,
    TPS,
    STRATEGY,
    SURVIVAL_HORROR,
    RACING,
    ACTION
}
