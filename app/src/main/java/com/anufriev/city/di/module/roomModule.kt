package com.anufriev.city.di.module

import android.content.Context
import com.anufriev.data.db.CityDatabase
import org.koin.dsl.module

val roomModule = module {

    single { provideDatabase(get()) }
    single { orgListDao(get()) }
    single { feedBackListDao(get()) }
}

fun orgListDao(prospectorDatabase: CityDatabase) = prospectorDatabase.getOrg()
fun feedBackListDao(prospectorDatabase: CityDatabase) = prospectorDatabase.getFeedBack()

fun provideDatabase(context: Context) = CityDatabase.buildDataSource(context)
