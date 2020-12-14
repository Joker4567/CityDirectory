package com.anufriev.city.di.module

import com.anufriev.data.db.entities.FeedBack
import com.anufriev.presentation.feedBack.FeedBackViewModel
import com.anufriev.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { (feedBacks: List<FeedBack>) -> FeedBackViewModel(feedBacks) }
}