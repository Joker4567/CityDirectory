package com.anufriev.presentation.splash

import androidx.lifecycle.MutableLiveData
import com.anufriev.utils.platform.BaseViewModel
import com.anufriev.utils.platform.Event
import com.anufriev.utils.platform.State

class SplashViewModel() : BaseViewModel() {
    val eventLoader = MutableLiveData<Event<State>>()

    init {
        //Загружаем список организаций из сети и сохраняем в локальную БД
        eventLoader.postValue(Event(State.Loaded))
    }

}