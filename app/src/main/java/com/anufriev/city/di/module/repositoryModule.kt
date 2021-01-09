package com.anufriev.city.di.module

import com.anufriev.data.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    single<OrganizationRepository>(createdAtStart = true, override = true) {
        OrganizationRepositoryImp(get(), get(), get())
    }
    single<FeedBackRepository>(createdAtStart = true, override = true) {
        FeedBackRepositoryImp(get(), get(), get())
    }
    single { ContactRepository(get()) }

    single<FirebaseRepository>(
        createdAtStart = true,
        override = true
    ) { FirebaseRepositoryImp(get(), get()) }

    single<FellowRepository>(
        createdAtStart = true,
        override = true
    ) { FellowRepositoryImp(get(), get(),get()) }
}