package com.anufriev.data.repository

import com.anufriev.data.db.entities.FellowDaoEntity
import com.anufriev.data.entity.Fellow
import com.anufriev.utils.platform.State

interface FellowRepository {
    //Получить сообщения попутчиков из сети
    suspend fun getFellowListNetwork(
        city:String,
        onSuccess: (List<Fellow>) -> Unit,
        onState: (State) -> Unit
    )
    //Добавить сообщение попутчика
    suspend fun setFellow(
        city:String,
        date:String,
        description:String,
        onSuccess: (Fellow) -> Unit,
        onState: (State) -> Unit
    )
    //Получить сообщения попутчиков из локальной БД
    suspend fun getFellowList(city:String):List<FellowDaoEntity>
    //Сохранить в локальную БД сообщение попутчика
    suspend fun setFellowList(list:List<Fellow>)

    suspend fun deleteAllFellows()
}