package com.anufriev.presentation.infoPhone

import androidx.lifecycle.MutableLiveData
import androidx.room.withTransaction
import com.anufriev.data.db.CityDatabase
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.repository.ContactRepository
import com.anufriev.data.repository.FeedBackRepository
import com.anufriev.data.repository.OrganizationRepository
import com.anufriev.utils.ext.getShortPhone
import com.anufriev.utils.platform.BaseViewModel
import com.anufriev.utils.platform.State

class InfoPhoneViewModel(
    private val feedBacksArg: OrganizationDaoEntity,
    private val repository: OrganizationRepository,
    private val contactRepository: ContactRepository
) : BaseViewModel() {
    var phoneList = MutableLiveData<List<String>>()
    var phoneDriver = MutableLiveData<Boolean>()
    var _phoneDriver = ""
    var _phone = ""
    init {
        initPhone()
    }

    private fun initPhone() {
        val listPhone = feedBacksArg.phoneNumber.trim().split(',')
        phoneList.postValue(listPhone)
    }

    fun removeCallLog(idOrg: Int) {
        launchIO {
            CityDatabase.instance.withTransaction {
                val listOrg = repository.getOrganization().filter { x -> x.id == idOrg }
                val listCall = contactRepository.getAllLogCall()
                listOrg.forEach { org ->
                    listCall.filter { contact -> contact.phones.contains(getShortPhone(org.phoneNumber)) }
                        .forEach { contactRepository.deleteCallNumberById(it.id) }
                }
            }
        }
    }

    fun checkPhoneDriver(idOrg: Int, phone: String, lat: Double, lon: Double) {
        launchIO {
            repository.checkPhoneDriver(idOrg, lat, lon, {
                if (it.contains(phone, ignoreCase = true)) {
                    _phone = phone
                    phoneDriver.postValue(false)
                } else {
                    _phoneDriver = it
                    phoneDriver.postValue(true)
                }

            }, {
                errorResponse(it, phone)
            })
        }
    }

    private fun errorResponse(it: State, phone: String) {
        handleState(it)
        if (it != State.Loaded && it != State.Loading) {
            _phone = phone
            phoneDriver.postValue(false)
        }
    }
}