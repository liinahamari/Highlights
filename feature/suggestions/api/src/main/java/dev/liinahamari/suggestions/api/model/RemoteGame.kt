package dev.liinahamari.suggestions.api.model

import com.google.gson.annotations.SerializedName
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.Game
import dev.liinahamari.api.domain.entities.GameGenre

data class SearchGameResponse(
    @SerializedName("count") val count: Int? = null,
    @SerializedName("next") val next: String? = null,
    @SerializedName("previous") val previous: String? = null,
    @SerializedName("results") val results: List<RemoteGame>? = null
)

data class RemoteGame(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("released") val releasedDate: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("tba") val tba: Boolean? = null,
    @SerializedName("background_image") val backgroundImageUri: String? = null,
    @SerializedName("metacritic") val metaCritic: Int? = null,
    @SerializedName("playtime") val playTime: Int? = null,
    @SerializedName("suggestions_count") val suggestionsCount: Int? = null,
    @SerializedName("updated") val updatedDate: String? = null,
    @SerializedName("user_game") val userGame: String? = null,
    @SerializedName("reviews_count") val reviewsCount: Int? = null,
    @SerializedName("saturated_color") val saturatedColor: String? = null,
    @SerializedName("dominant_color") val dominantColor: String? = null,
    @SerializedName("platforms") val platforms: List<PlatformMetaDataResponse>? = null,
    @SerializedName("parent_platforms") val parentPlatforms: List<ParentPlatformMetaDataResponse>? = null,
    @SerializedName("genres") val genres: List<GenreDetailResponse>? = null,
    @SerializedName("clip") val clip: String? = null,
    @SerializedName("tags") val tags: List<TagDetailResponse>? = null,
    @SerializedName("short_screenshots") val shortScreenshots: List<ShortScreenshotDetailResponse>? = null
)

data class ShortScreenshotDetailResponse(
    @SerializedName("id") val screenshotId: Int? = null,
    @SerializedName("image") val screenshotImage: String? = null
)


data class ParentPlatformMetaDataResponse(
    @SerializedName("platform") val parentPlatform: ParentPlatformDetailResponse? = null
)

data class ParentPlatformDetailResponse(
    @SerializedName("id") val parentPlatformId: Int? = null,
    @SerializedName("name") val parentPlatformName: String? = null,
    @SerializedName("slug") val parentPlatformSlug: String? = null
)

data class PlatformMetaDataResponse(
    @SerializedName("platform") val platform: PlatformDetailResponse? = null,
    @SerializedName("released_at") val releasedAt: String? = null,
    @SerializedName("requirements_en") val requirementsEnglish: RequirementDetailResponse? = null,
    @SerializedName("requirements_ru") val requirementsRussian: RequirementDetailResponse? = null
)

data class RequirementDetailResponse(
    @SerializedName("minimum") val minimum: String? = null,
    @SerializedName("recommended") val recommended: String? = null
)


data class PlatformDetailResponse(
    @SerializedName("id") val platformId: Int? = null,
    @SerializedName("slug") val platformSlug: String? = null,
    @SerializedName("name") val platformName: String? = null,
    @SerializedName("image") val platformImage: String? = null,
    @SerializedName("games") val platformGames: List<PlatformGame>? = null,
    @SerializedName("year_end") val platformEndYear: Int? = null,
    @SerializedName("year_start") val platformStartYear: Int? = null,
    @SerializedName("games_count") val platformGamesCount: Int? = null,
    @SerializedName("image_background") val platformImageBackground: String? = null
)

data class PlatformGame(
    @SerializedName("id") val gameId: Int? = null,
    @SerializedName("slug") val gameSlug: String? = null,
    @SerializedName("name") val gameName: String? = null,
    @SerializedName("added") val gameAddedCount: Int? = null,
)

data class TagDetailResponse(
    @SerializedName("id") val tagId: Int? = null,
    @SerializedName("name") val tagName: String? = null,
    @SerializedName("slug") val tagSlug: String? = null,
    @SerializedName("language") val tagLanguage: String? = null,
    @SerializedName("games_count") val tagGamesCount: Int? = null,
    @SerializedName("image_background") val tagImageBackground: String? = null,
    @SerializedName("games") val tagGames: List<TagGame>? = null
)

data class TagGame(
    @SerializedName("id") val gameId: Int? = null,
    @SerializedName("slug") val gameSlug: String? = null,
    @SerializedName("name") val gameName: String? = null,
    @SerializedName("added") val gameAddedCount: Int? = null,
)

data class GenreDetailResponse(
    @SerializedName("id") val genreId: Int? = null,
    @SerializedName("name") val genreName: String? = null,
    @SerializedName("slug") val genreSlug: String? = null,
    @SerializedName("games_count") val genreGamesCount: Int? = null,
    @SerializedName("image_background") val genreImageBackground: String? = null,
    @SerializedName("games") val genreGames: List<GenreGame>? = null
)

data class GenreGame(
    @SerializedName("id") val gameId: Int? = null,
    @SerializedName("slug") val gameSlug: String? = null,
    @SerializedName("name") val gameName: String? = null,
    @SerializedName("added") val gameAddedCount: Int? = null,
)

fun RemoteGame.toDomain(category: Category, genres: List<GameGenre> = listOf()): Game = Game(
    category = category,
    genres = genres,
    name = this.name!!,
    description = description ?: "",
    posterUrl = shortScreenshots?.firstOrNull { it.screenshotImage.isNullOrBlank().not() }?.screenshotImage ?: "",
    year = releasedDate?.substring(0, 4)?.toInt() ?: 0,
    id = 0L
)
