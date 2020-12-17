package com.anufriev.presentation.home

import androidx.lifecycle.MutableLiveData
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.repository.OrganizationRepository
import com.anufriev.utils.platform.BaseViewModel
import com.anufriev.utils.platform.State

class HomeViewModel(
    private val repository: OrganizationRepository
) : BaseViewModel() {

    var works = MutableLiveData<List<OrganizationDaoEntity>>()

    init {
        //Загружаем список организаций из локальной базы данных
        //при обновлении swipeRefresh апршиваем новые данные из сети
        getOrg()
    }

    fun getOrg(){
        launchIO {
            repository.getOrg({
                launchIO {
                    repository.setOrganization(it)
                    val list = repository.getOrganization()
                    launch {
                        works.postValue(list)
                    }
                }

            },{
                launchIO {
                    if(it != State.Loaded && it != State.Loading) {
                        val list = repository.getOrganization()
                        launch {
                            works.postValue(list)
                        }
                    }
                }
            })
        }
    }
}