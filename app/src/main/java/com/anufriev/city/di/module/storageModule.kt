package com.anufriev.city.di.module

import android.content.Context
import com.anufriev.data.storage.Pref
import org.koin.dsl.module

val storageModule = module {

    single { provideSharedPreferences(get()) }
}

fun provideSharedPreferences(context: Context): Pref {
    return Pref(context)
}