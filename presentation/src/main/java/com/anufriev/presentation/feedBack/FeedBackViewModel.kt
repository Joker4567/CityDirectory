package com.anufriev.presentation.feedBack

import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import com.anufriev.data.db.entities.FeedBackDaoEntity
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.repository.FeedBackRepository
import com.anufriev.utils.ext.getCurrentDateTime
import com.anufriev.utils.ext.toString
import com.anufriev.utils.platform.BaseViewModel
import com.anufriev.utils.platform.State

class FeedBackViewModel(
    private val feedBacksArg: OrganizationDaoEntity,
    private val repository: FeedBackRepository
) : BaseViewModel() {
    var feedBacks = MutableLiveData<List<FeedBackDaoEntity>>()
    var error = MutableLiveData<String>()

    init {
        getFeedBackList()
    }

    private fun getFeedBackList() {
        launchIO {
            repository.getFeedBackListNetwork(feedBacksArg.id,
                {
                    launchIO {
                        repository.setFeedBackList(it, feedBacksArg.id, "")
                        val list = repository.getFeedBackList(feedBacksArg.id).sortedByDescending { x -> x.id }
                        launch {
                            feedBacks.postValue(list)
                        }
                    }
                },
                {
                    if (it != State.Loading && it != State.Loaded) {
                        launchIO {
                            val list = repository.getFeedBackList(feedBacksArg.id).sortedByDescending { x -> x.id }
                            launch {
                                feedBacks.postValue(list)
                            }
                        }
                    }
                })
        }
    }

    fun setFeedBack(flag: Boolean, text: String) {
        launchIO {
            val feedBackItem =
                repository.getLastFeedBack(feedBacksArg.id, Settings.Secure.ANDROID_ID)
            if (feedBackItem != null) {
                if (System.currentTimeMillis() >= feedBackItem.time + 86400) {
                    setFeedBackLocal(flag, text,  getCurrentDateTime().toString("dd.MM.yyyy HH:mm"))
                } else {
                    error.postValue("Ожидайте сутки, для добавления нового отзыва")
                }
            } else setFeedBackLocal(flag, text,  getCurrentDateTime().toString("dd.MM.yyyy HH:mm"))

        }
    }

    private fun setFeedBackLocal(flag: Boolean, text: String, date: String) {
        //Можем заносить данный отзыв
        launchIO {
            repository.setRatingReviews(
                flag, text, date,
                feedBacksArg.id,
                {
                    launchIO {
                        repository.setFeedBackList(
                            listOf(it),
                            feedBacksArg.id,
                            Settings.Secure.ANDROID_ID
                        )
                        val list = repository.getFeedBackList(feedBacksArg.id)
                        launch {
                            feedBacks.postValue(list)
                        }
                        val feedBackLast =
                            repository.getLastFeedBack(feedBacksArg.id, Settings.Secure.ANDROID_ID)
                        repository.updateFeedBack(
                            FeedBackDaoEntity(
                                feedBackLast.id,
                                feedBackLast.idOrg,
                                feedBackLast.state,
                                feedBackLast.description,
                                System.currentTimeMillis(),
                                feedBackLast.uid,
                                feedBackLast.date
                            )
                        )
                    }
                }, ::handleState
            )
        }
    }
}