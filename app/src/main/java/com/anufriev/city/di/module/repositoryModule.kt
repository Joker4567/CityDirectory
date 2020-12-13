package com.anufriev.city.di.module

import com.anufriev.data.repository.OrganizationRepository
import com.anufriev.data.repository.OrganizationRepositoryImp
import org.koin.dsl.module

val repositoryModule = module {
    single<OrganizationRepository>(createdAtStart = true, override = true) {
        OrganizationRepositoryImp()
    }
}
