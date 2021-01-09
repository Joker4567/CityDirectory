package com.anufriev.presentation.fellowAdd

import androidx.lifecycle.MutableLiveData
import androidx.room.withTransaction
import com.anufriev.data.db.CityDatabase
import com.anufriev.data.repository.FellowRepository
import com.anufriev.utils.ext.getCurrentDateTime
import com.anufriev.utils.ext.toString
import com.anufriev.utils.platform.BaseViewModel
import com.anufriev.utils.platform.Event

class FellowAddViewModel(
    private val repository: FellowRepository
) : BaseViewModel() {

    val success = MutableLiveData<String>()

    fun addRecord(text: String, city: String) {
        launchIO {
            repository.setFellow(city, getCurrentDateTime().toString("dd.MM.yyyy"), text, {
                launchIO {
                    CityDatabase.instance.withTransaction {
                        repository.setFellowList(listOf(it))
                        success.postValue("Запись успешно добавлена")
                    }
                }
            }, ::handleState)
        }
    }
}