package com.anufriev.data.repository

import com.anufriev.data.db.entities.PhoneCallDaoEntity

interface PhoneCallRepository {
    suspend fun getPhoneCallLast(uid:String, idOrg:Int):PhoneCallDaoEntity
    suspend fun setPhoneCallList(list:List<PhoneCallDaoEntity>)
}