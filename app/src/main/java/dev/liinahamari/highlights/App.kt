package dev.liinahamari.highlights

import android.app.Application
import com.github.anrwatchdog.ANRWatchDog

class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        ANRWatchDog(3000).start()
    }
}
