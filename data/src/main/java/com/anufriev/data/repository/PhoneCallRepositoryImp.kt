package com.anufriev.data.repository

import com.anufriev.data.db.dao.PhoneCallDao
import com.anufriev.data.db.entities.PhoneCallDaoEntity

class PhoneCallRepositoryImp(
    private val repository: PhoneCallDao
) : PhoneCallRepository {
    override suspend fun getPhoneCallLast(uid: String, idOrg:Int): PhoneCallDaoEntity =
        repository.getPhoneCallList(uid, idOrg)

    override suspend fun setPhoneCallList(list: List<PhoneCallDaoEntity>) {
        repository.setPhoneCallList(list)
    }
}