package com.anufriev.city.di.module

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.anufriev.data.network.ApiService
import com.anufriev.data.network.interceptor.HeaderInterceptor
import com.anufriev.data.network.interceptor.ValidationInterceptor
import com.anufriev.data.storage.Pref
import com.anufriev.city.BuildConfig
import com.anufriev.data.network.NotificationAPI
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val TIME_OUT = 60L


val networkModule = module {

    single { buildRetrofit(get(), get()) }

    single { buildOkHttp(get(), get()) }

    single { provideCache(androidApplication()) }

    single { provideHeaderInterceptor(get()) }

    single { provideValidationInterceptor(get()) }

    single { provideApiService(get()) }

    single { buildJson() }

    single { provideFirebaseApi(get()) }
}

fun provideCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024
    return Cache(application.cacheDir, cacheSize.toLong())
}

private fun buildOkHttp(cache:Cache, headerInterceptor: HeaderInterceptor): OkHttpClient {
    val okHttpClientBuilder = OkHttpClient.Builder()
    with(okHttpClientBuilder){
        cache(cache)
        connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        readTimeout(TIME_OUT, TimeUnit.SECONDS)
        addInterceptor(headerInterceptor)
        retryOnConnectionFailure(true)
    }
    if (BuildConfig.DEBUG) {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClientBuilder.addInterceptor(loggingInterceptor)
        okHttpClientBuilder.addNetworkInterceptor(StethoInterceptor())
    }

    return okHttpClientBuilder.build()
}

private fun buildRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_ENDPOINT)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

private fun provideHeaderInterceptor(pref: Pref) = HeaderInterceptor(pref)

private fun provideValidationInterceptor(gson: Gson) = ValidationInterceptor(gson)

private fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

private fun provideFirebaseApi(retrofit: Retrofit): NotificationAPI = retrofit.create(NotificationAPI::class.java)

private fun buildJson() = GsonBuilder().create()

