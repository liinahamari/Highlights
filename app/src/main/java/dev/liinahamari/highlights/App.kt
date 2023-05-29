package dev.liinahamari.highlights

import android.app.Application
import androidx.room.Room
import dev.liinahamari.highlights.db.DATABASE_NAME
import dev.liinahamari.highlights.db.EntriesDatabase

class App : Application() {
    lateinit var db: EntriesDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            EntriesDatabase::class.java, DATABASE_NAME,
        ).build()
    }
}
