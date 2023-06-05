package dev.liinahamari.highlights

import android.app.Application
import com.github.anrwatchdog.ANRWatchDog
import dev.liinahamari.highlights.di.components.DaggerMainComponent
import dev.liinahamari.highlights.di.components.MainComponent

class App : Application() {
    lateinit var appComponent: MainComponent
    override fun onCreate() {
        appComponent = DaggerMainComponent.builder().application(this).build()
        super.onCreate()
        ANRWatchDog(3000).start()
    }
}
