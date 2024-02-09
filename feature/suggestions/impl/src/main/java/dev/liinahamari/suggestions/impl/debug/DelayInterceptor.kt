package dev.liinahamari.suggestions.impl.debug

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

/** usage :
 * OkHttpClient.Builder()
 *   .addInterceptor(DelayInterceptor(3000L, TimeUnit.MILLISECONDS))
 * */
class DelayInterceptor(duration: Long, timeUnit: TimeUnit) : Interceptor {
    private var delayMillis: Long = 0L

    init {
        require(duration >= 0) { "duration can't be negative value." }
        delayMillis = timeUnit.toMillis(duration)
    }

    @Throws(IOException::class) override fun intercept(chain: Interceptor.Chain): Response {
        if (delayMillis > 0) {
            try {
                sleep(delayMillis)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        return chain.proceed(chain.request())
    }
}
