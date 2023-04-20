package dev.liinahamari.highlights

import android.app.Application
import androidx.room.Room
import dev.liinahamari.highlights.db.EntriesDatabase

class App : Application() {
    lateinit var db: EntriesDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            EntriesDatabase::class.java, "entries-db",
        ).allowMainThreadQueries().build()
    }
}
