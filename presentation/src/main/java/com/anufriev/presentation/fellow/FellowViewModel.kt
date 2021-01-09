package com.anufriev.presentation.fellow

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.withTransaction
import com.anufriev.data.db.CityDatabase
import com.anufriev.data.db.entities.FellowDaoEntity
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.repository.FellowRepository
import com.anufriev.data.storage.Pref
import com.anufriev.utils.platform.BaseViewModel
import com.anufriev.utils.platform.State

class FellowViewModel(
    private val repository: FellowRepository,
    private val context: Context
) : BaseViewModel() {
    var fellowList = MutableLiveData<List<FellowDaoEntity>>()

    fun getFellowList(city: String) {
        launchIO {
            repository.getFellowListNetwork(
                city, {
                    launchIO {
                        CityDatabase.instance.withTransaction {
                            repository.deleteAllFellows()//Очищаем старые записи
                            repository.setFellowList(it)
                        }
                        val list =
                            repository.getFellowList(city).sortedByDescending { x -> x.id }
                        launch {
                            fellowList.postValue(list)
                        }
                    }
                }, { error(it, city) }
            )
        }
    }

    private fun error(it: State, city: String) {
        handleState(it)
        launchIO {
            if (it != State.Loaded && it != State.Loading) {
                val list = repository.getFellowList(city).sortedByDescending { x -> x.id }
                launch {
                    fellowList.postValue(list)
                }
            }
        }
    }
}