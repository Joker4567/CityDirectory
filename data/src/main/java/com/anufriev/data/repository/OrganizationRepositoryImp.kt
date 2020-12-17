package com.anufriev.data.repository

import com.anufriev.data.db.dao.OrganizationDao
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.entity.Organization
import com.anufriev.data.network.ApiService
import com.anufriev.utils.platform.BaseRepository
import com.anufriev.utils.platform.ErrorHandler
import com.anufriev.utils.platform.State
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class OrganizationRepositoryImp(
    errorHandler: ErrorHandler,
    private val api: ApiService,
    private val orgDao: OrganizationDao
) : BaseRepository(errorHandler = errorHandler), OrganizationRepository {

    //Получить список организаций из локальной БД
    override suspend fun getOrganization(): List<OrganizationDaoEntity> = orgDao.getOrgList()

    // Установить рейтинг организации
    override suspend fun setRating(
        id: Int,
        flag: Boolean,
        onSuccess: (Boolean) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState){
            val jsonObjectString = if(flag)
                "true"
            else
                "false"
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            api.setRating(id, requestBody)
            true
        }
    }

    //Сохранить список организаций в локальную БД
    override suspend fun setOrganization(
        list: List<Organization>
    ) = orgDao.setOrgList(list.map { it.from() })

    //Получить список организаций с удаленного сервера
    override suspend fun getOrg(
        onSuccess: (List<Organization>) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            api.getOrg()
        }
    }
}