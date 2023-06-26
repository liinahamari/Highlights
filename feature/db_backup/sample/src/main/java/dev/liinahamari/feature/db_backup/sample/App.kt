package dev.liinahamari.feature.db_backup.sample

import android.app.Application
import android.os.StrictMode
import androidx.room.Room
import dev.liinahamari.db_backup.sample.BuildConfig

class App : Application() {
    lateinit var db: TestDb

    override fun onCreate() {
        super.onCreate()
        setupDb()
        enableStrictMode()
    }

    private fun setupDb() {
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

    private fun enableStrictMode() {
        if (BuildConfig.DEBUG.not()) return
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }
}
