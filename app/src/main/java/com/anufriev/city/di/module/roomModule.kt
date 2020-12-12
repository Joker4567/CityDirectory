package com.anufriev.city.di.module

import android.content.Context
import com.anufriev.data.db.CityDatabase
import org.koin.dsl.module

val roomModule = module {

    single { provideDatabase(get()) }
}

fun provideDatabase(context: Context) = CityDatabase.buildDataSource(context)
