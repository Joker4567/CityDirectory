package com.anufriev.presentation.home

import androidx.lifecycle.MutableLiveData
import com.anufriev.data.db.entities.Organization
import com.anufriev.data.repository.OrganizationRepository
import com.anufriev.utils.platform.BaseViewModel

class HomeViewModel(
    private val repository: OrganizationRepository
) : BaseViewModel() {

    var works = MutableLiveData<List<Organization>>()

    init {
        //Загружаем список организаций из локальной базы данных
        //при обновлении swipeRefresh апршиваем новые данные из сети
        getOrg()
    }

    private fun getOrg(){
        works.postValue(repository.getOrganization())
    }
}