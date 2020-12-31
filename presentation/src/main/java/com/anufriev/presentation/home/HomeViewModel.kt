package com.anufriev.presentation.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.withTransaction
import com.anufriev.data.db.CityDatabase
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.repository.OrganizationRepository
import com.anufriev.data.storage.Pref
import com.anufriev.utils.platform.BaseViewModel
import com.anufriev.utils.platform.State

class HomeViewModel(
    private val repository: OrganizationRepository
) : BaseViewModel() {

    var works = MutableLiveData<List<OrganizationDaoEntity>>()

    init {
        //Загружаем список организаций из локальной базы данных
        //при обновлении swipeRefresh апршиваем новые данные из сети
    }

    fun getOrg(lat:Double, lon:Double, context: Context){
        launchIO {
            repository.getCity(lat, lon, {
                getOrg(it.suggestions.first().data.city)
                Pref(context).city = it.suggestions.first().data.city
            }, ::error)

        }
    }

    fun getOrg(city:String){
        launchIO {
            repository.getOrg(city,
                {
                    launchIO {
                        CityDatabase.instance.withTransaction {
                            repository.deleteAllOrg()//Очищаем старые записи
                            repository.setOrganization(it)
                        }
                        val list = repository.getOrganization()
                        launch {
                            works.postValue(list)
                        }
                    }
                }, ::error
            )
        }
    }

    private fun error(it:State){
        handleState(it)
        launchIO {
            if(it != State.Loaded && it != State.Loading) {
                val list = repository.getOrganization()
                launch {
                    works.postValue(list)
                }
            }
        }
    }
}