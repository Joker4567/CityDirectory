package com.anufriev.city.di.module

import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.presentation.feedBack.FeedBackViewModel
import com.anufriev.presentation.home.HomeViewModel
import com.anufriev.presentation.resultCall.ResultCallViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ResultCallViewModel(get()) }
    viewModel { (feedBacks: OrganizationDaoEntity) -> FeedBackViewModel(feedBacks, get()) }
}