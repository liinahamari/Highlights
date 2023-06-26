package dev.liinahamari.feature.db_backup.sample

import android.app.Application
import androidx.room.Room

class App : Application() {
    lateinit var db: TestDb

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            TestDb::class.java, "test",
        ).allowMainThreadQueries()
            .build()
            .also {
                it.bookDao()
                    .insert(Book())
                    .subscribe()
            }
    }
}
