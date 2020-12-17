package com.anufriev.city.di.module

import com.anufriev.data.repository.FeedBackRepository
import com.anufriev.data.repository.FeedBackRepositoryImp
import com.anufriev.data.repository.OrganizationRepository
import com.anufriev.data.repository.OrganizationRepositoryImp
import org.koin.dsl.module

val repositoryModule = module {
    single<OrganizationRepository>(createdAtStart = true, override = true) {
        OrganizationRepositoryImp(get(), get(), get())
    }
    single<FeedBackRepository>(createdAtStart = true, override = true) {
        FeedBackRepositoryImp(get(), get(), get())
    }
}