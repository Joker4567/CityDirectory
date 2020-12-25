package com.anufriev.data.repository

import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.entity.GeoCity
import com.anufriev.data.entity.Organization
import com.anufriev.utils.platform.State

interface OrganizationRepository {
    suspend fun getOrg(
        city:String,
        onSuccess: (List<Organization>) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun setOrganization(
        list: List<Organization>
    )

    suspend fun getOrganization(): List<OrganizationDaoEntity>

    suspend fun setRating(
        id:Int,
        flag:Boolean,
        onSuccess: (Boolean) -> Unit,
        onState: (State) -> Unit)

    suspend fun getCity(
        lat:Double,
        lon:Double,
        onSuccess: (GeoCity) -> Unit,
        onState: (State) -> Unit)
}