package com.anufriev.presentation.resultCall

import android.util.Log
import com.anufriev.data.repository.OrganizationRepository
import com.anufriev.utils.platform.BaseViewModel

class ResultCallViewModel(private val repository: OrganizationRepository) : BaseViewModel() {

    fun setRating(flag:Boolean, id:Int){
        launchIO {
            repository.setRating(id, flag, {
                Log.d("Rating","Рейтинг организации успешно изменён")
            }, ::handleState)
        }
    }

}