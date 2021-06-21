package com.anufriev.city

import android.app.Application
import android.content.Context
import android.util.Log
import com.anufriev.city.di.module.*
import com.anufriev.city.service.FirebaseService
import com.anufriev.utils.services.CloudMessage.Companion.TOPIC
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    //Логирование фатальных ошибок в базу данных Firebase
    private var mInstance: FirebaseCrashlytics? = null

    override fun onCreate() {
        super.onCreate()
        mInstance = FirebaseCrashlytics.getInstance()

        FirebaseService._sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        try {
            Thread
                .setDefaultUncaughtExceptionHandler { thread: Thread, throwable: Throwable ->
                    handleUncaughtException(thread, throwable)
                }
        } catch (ex: SecurityException) {
            Log.e(
                "App",
                "Could not set the Default Uncaught Exception Handler:" + ex.stackTrace
            )
        }
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    appModule,
                    networkModule,
                    repositoryModule,
                    storageModule,
                    viewModelModule,
                    roomModule
                )
            )
        }
    }

    //Ловим все критические ошибки в приложении
    private fun handleUncaughtException(thread: Thread, ex: Throwable) {
        if (!BuildConfig.DEBUG) {
            mInstance?.setCustomKey("Version", "Релизная версия")
            mInstance?.setUserId("UserId")
            mInstance?.recordException(ex)
        }
    }
}