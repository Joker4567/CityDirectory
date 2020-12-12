package com.anufriev.presentation.home

import androidx.lifecycle.MutableLiveData
import com.anufriev.utils.platform.BaseViewModel

class HomeViewModel() : BaseViewModel() {

    var works = MutableLiveData<List<Object>>()

    init {
        //Загружаем список организаций из локальной базы данных
        //при обновлении swipeRefresh апршиваем новые данные из сети
    }
}