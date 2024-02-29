package dev.liinahamari.suggestions.impl.di.modules

import dagger.Module
import dagger.Provides
import dev.liinahamari.suggestions.impl.BuildConfig
import dev.liinahamari.suggestions.impl.data.apis.books.SearchGoogleBooksApi
import dev.liinahamari.suggestions.impl.data.apis.SearchGameApi
import dev.liinahamari.suggestions.impl.data.apis.SearchMovieApi
import dev.liinahamari.suggestions.impl.data.apis.books.SearchOpenLibraryApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL_TMDB = "https://api.themoviedb.org/3/"
private const val BASE_URL_GAMES_DB = "https://api.rawg.io/api/"
private const val BASE_URL_OPEN_LIBRARY = "https://openlibrary.org/"
private const val DEFAULT_TIMEOUT_AMOUNT_IN_SEC = 20L
private const val CONNECTION_TIMEOUT_QUALIFIER = "connection_timeout"
private const val GOOGLE_BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/"

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesCallAdapterFactory(): CallAdapter.Factory = RxJava3CallAdapterFactory.create()

    @Singleton
    @Provides
    @Named(CONNECTION_TIMEOUT_QUALIFIER)
    fun provideConnectionTimeout(): Long = if (BuildConfig.DEBUG) 10L else DEFAULT_TIMEOUT_AMOUNT_IN_SEC

    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(BODY)

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @Named(CONNECTION_TIMEOUT_QUALIFIER) timeout: Long
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .callTimeout(timeout, TimeUnit.SECONDS)
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun providesMovieApiService(
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

    @Singleton
    @Provides
    fun providesGoogleBookApiService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory
    ): SearchGoogleBooksApi =
        Retrofit.Builder()
            .baseUrl(GOOGLE_BOOKS_BASE_URL)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(SearchGoogleBooksApi::class.java)

    @Singleton
    @Provides
    fun providesOpenLibraryBookApiService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory
    ): SearchOpenLibraryApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL_OPEN_LIBRARY)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(SearchOpenLibraryApi::class.java)

    @Singleton
    @Provides
    fun providesGameApiService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory
    ): SearchGameApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL_GAMES_DB)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(SearchGameApi::class.java)
}
