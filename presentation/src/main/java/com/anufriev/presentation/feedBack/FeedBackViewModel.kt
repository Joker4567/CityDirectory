package com.anufriev.presentation.feedBack

import androidx.lifecycle.MutableLiveData
import com.anufriev.data.db.entities.FeedBack
import com.anufriev.utils.platform.BaseViewModel

class FeedBackViewModel(
    private val feedBacksArg: List<FeedBack>
) : BaseViewModel() {
    var feedBacks = MutableLiveData<List<FeedBack>>()

    init {
        setFeedBackList()
    }

    private fun setFeedBackList(){
        feedBacks.postValue(feedBacksArg)
    }
}