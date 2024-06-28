package dev.liinahamari.impl.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

//fixme static nosql?
@Entity
data class Country(@PrimaryKey val isoCountryCode: String, val countryEnglishName: String)

@Dao interface CachedCountriesDao {
    @Query("SELECT * FROM country") suspend fun getAll(): List<Country>

    @Insert suspend fun insertAll(countries: List<Country>)

    @Query("SELECT COUNT(isoCountryCode) FROM country")
    suspend fun getRowCount(): Int
}

@Database(entities = [Country::class], version = 1)
abstract class CachedCountriesDatabase : RoomDatabase() {
    abstract fun cachedCountriesDao(): CachedCountriesDao
}

