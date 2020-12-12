package com.anufriev.city.di.module

import com.anufriev.presentation.home.HomeViewModel
import com.anufriev.presentation.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel() }
    viewModel { SplashViewModel() }
}