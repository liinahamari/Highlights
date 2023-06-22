package dev.liinahamari.highlights.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.liinahamari.highlights.db.daos.BookGenre
import dev.liinahamari.highlights.db.daos.GameGenre
import dev.liinahamari.highlights.db.daos.MovieGenre
import dev.liinahamari.highlights.ui.single_entity.EntityCategory

class CategoryConverters {
    @TypeConverter
    fun toCategory(value: String) = enumValueOf<EntityCategory>(value)

    @TypeConverter
    fun fromCategory(value: EntityCategory) = value.name
}

class MovieGenreConverters {
    @TypeConverter
    fun fromCategory(values: List<MovieGenre>): String =
        Gson().toJson(values, object : TypeToken<List<MovieGenre>>() {}.type)

    @TypeConverter
    fun toCategory(value: String): List<MovieGenre> =
        Gson().fromJson(value, object : TypeToken<List<MovieGenre>>() {}.type)
}

class GameGenreConverters {
    @TypeConverter
    fun fromCategory(values: List<GameGenre>): String =
        Gson().toJson(values, object : TypeToken<List<GameGenre>>() {}.type)

    @TypeConverter
    fun toCategory(value: String): List<GameGenre> =
        Gson().fromJson(value, object : TypeToken<List<GameGenre>>() {}.type)
}

class BookGenreConverters {
    @TypeConverter
    fun fromCategory(values: List<BookGenre>): String =
        Gson().toJson(values, object : TypeToken<List<BookGenre>>() {}.type)

    @TypeConverter
    fun toCategory(value: String): List<BookGenre> =
        Gson().fromJson(value, object : TypeToken<List<BookGenre>>() {}.type)
}

class CountryConverter {
    @TypeConverter fun fromString(value: String): Array<String> =
        Gson().fromJson(value, object : TypeToken<Array<String>>() {}.type/*fixme '_'*/)

    @TypeConverter fun fromArrayList(array: Array<String>): String = Gson().toJson(array)
}
