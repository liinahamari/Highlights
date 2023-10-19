package dev.liinahamari.impl.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.liinahamari.api.domain.entities.BookGenre
import dev.liinahamari.api.domain.entities.Category
import dev.liinahamari.api.domain.entities.GameGenre
import dev.liinahamari.api.domain.entities.MovieGenre

class CategoryConverters {
    @TypeConverter
    fun toCategory(value: String) = enumValueOf<Category>(value)

    @TypeConverter
    fun fromCategory(value: Category) = value.name
}

class MovieGenreConverters {
    @TypeConverter
    fun fromCategory(values: List<MovieGenre>): String =
        Gson().toJson(values, object : TypeToken<List<MovieGenre>>() {}.type)

    @TypeConverter
    fun toCategory(value: String): List<MovieGenre> =
        Gson().fromJson(value.replace(' ', '_'), object : TypeToken<List<MovieGenre>>() {}.type)
}

class GameGenreConverters {
    @TypeConverter
    fun fromCategory(values: List<GameGenre>): String =
        Gson().toJson(values, object : TypeToken<List<GameGenre>>() {}.type)

    @TypeConverter
    fun toCategory(value: String): List<GameGenre> =
        Gson().fromJson(value.replace(' ', '_'), object : TypeToken<List<GameGenre>>() {}.type)
}

class BookGenreConverters {
    @TypeConverter
    fun fromCategory(values: List<BookGenre>): String =
        Gson().toJson(values, object : TypeToken<List<BookGenre>>() {}.type)

    @TypeConverter
    fun toCategory(value: String): List<BookGenre> =
        Gson().fromJson(value.replace(' ', '_'), object : TypeToken<List<BookGenre>>() {}.type)
}

class CountryConverter {
    @TypeConverter fun fromString(value: String): Array<String> =
        Gson().fromJson(value.replace(' ', '_'), object : TypeToken<Array<String>>() {}.type/*fixme '_'*/)

    @TypeConverter fun fromArrayList(array: Array<String>): String = Gson().toJson(array)
}
