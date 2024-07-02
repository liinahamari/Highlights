package dev.liinahamari.highlights

import android.app.Application
import android.content.Context
import dev.liinahamari.crash_screen.CrashInterceptor
import dev.liinahamari.crash_screen.api.CrashScreenDependencies

class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        ANRWatchDog(3000).start()

        if (BuildConfig.DEBUG) {
            CrashInterceptor.init(object : CrashScreenDependencies {
                override val context: Context = applicationContext
                override val doWhileImpossibleToStartCrashScreen: (Throwable) -> Unit
                    get() = {}
                override val doOnCrash: (Throwable) -> Unit = { it.printStackTrace() }
            })
        }
    }
}
