package com.anufriev.city.di.module

import android.content.Context
import com.anufriev.data.db.CityDatabase
import org.koin.dsl.module

val roomModule = module {

    single { provideDatabase(get()) }
    single { orgListDao(get()) }
    single { feedBackListDao(get()) }
    single { fellowListDao(get()) }
    single { phoneCallListDao(get()) }
}

fun orgListDao(prospectorDatabase: CityDatabase) = prospectorDatabase.getOrg()
fun feedBackListDao(prospectorDatabase: CityDatabase) = prospectorDatabase.getFeedBack()
fun fellowListDao(prospectorDatabase: CityDatabase) = prospectorDatabase.getFellow()
fun phoneCallListDao(prospectorDatabase: CityDatabase) = prospectorDatabase.getPhoneCall()

fun provideDatabase(context: Context) = CityDatabase.buildDataSource(context)
