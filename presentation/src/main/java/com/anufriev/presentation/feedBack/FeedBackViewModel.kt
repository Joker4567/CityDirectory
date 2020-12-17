package com.anufriev.presentation.feedBack

import androidx.lifecycle.MutableLiveData
import com.anufriev.data.db.entities.FeedBackDaoEntity
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.repository.FeedBackRepository
import com.anufriev.utils.platform.BaseViewModel
import com.anufriev.utils.platform.State

class FeedBackViewModel(
    private val feedBacksArg: OrganizationDaoEntity,
    private val repository: FeedBackRepository
) : BaseViewModel() {
    var feedBacks = MutableLiveData<List<FeedBackDaoEntity>>()

    init {
        setFeedBackList()
    }

    private fun setFeedBackList(){
        launchIO {
            repository.getFeedBackListNetwork(feedBacksArg.id,
                {
                    launchIO {
                        repository.setFeedBackList(it, feedBacksArg.id)
                        val list = repository.getFeedBackList(feedBacksArg.id)
                        launch {
                            feedBacks.postValue(list)
                        }
                    }
                },
                {
                    if(it != State.Loading && it != State.Loaded){
                        launchIO {
                            val list = repository.getFeedBackList(feedBacksArg.id)
                            launch {
                                feedBacks.postValue(list)
                            }
                        }
                    }
                })
        }
    }

    fun setFeedBack(flag:Boolean, text:String){
        launchIO {
            repository.setRatingReviews(flag, text,
                feedBacksArg.id,
                {
                    launchIO {
                        repository.setFeedBackList(listOf(it),feedBacksArg.id)
                        val list = repository.getFeedBackList(feedBacksArg.id)
                        launch {
                            feedBacks.postValue(list)
                        }
                    }
                },::handleState)
        }
    }
}