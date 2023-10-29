package dev.liinahamari.movies_suggestions.impl.di.modules

import dagger.Module
import dagger.Provides
import dev.liinahamari.movies_suggestions.impl.data.SearchMovieApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL_TMDB = "https://api.themoviedb.org/3/"
private const val DEFAULT_TIMEOUT_AMOUNT_IN_SEC = 15L

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesCallAdapterFactory(): CallAdapter.Factory = RxJava3CallAdapterFactory.create()

    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .callTimeout(DEFAULT_TIMEOUT_AMOUNT_IN_SEC, TimeUnit.SECONDS)
        .connectTimeout(DEFAULT_TIMEOUT_AMOUNT_IN_SEC, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT_AMOUNT_IN_SEC, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_TIMEOUT_AMOUNT_IN_SEC, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun providesAPIService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory
    ): SearchMovieApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL_TMDB)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(SearchMovieApi::class.java)
}
