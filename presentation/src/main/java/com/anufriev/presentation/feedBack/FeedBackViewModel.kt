package com.anufriev.presentation.feedBack

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anufriev.data.db.entities.FeedBackDaoEntity
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.entity.cloudMessageEntity.NotificationData
import com.anufriev.data.repository.FeedBackRepository
import com.anufriev.data.repository.FirebaseRepository
import com.anufriev.data.storage.Pref
import com.anufriev.utils.platform.BaseViewModel
import com.anufriev.utils.platform.State
import com.anufriev.utils.services.CloudMessage.Companion.AUTH_KEY
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class FeedBackViewModel(
    private val feedBacksArg: OrganizationDaoEntity,
    private val repository: FeedBackRepository,
    private val firebaseApi: FirebaseRepository,
    private val context: Context
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
                        val list = repository.getFeedBackList(feedBacksArg.id)
                            .sortedByDescending { x -> x.id }
                        launch {
                            feedBacks.postValue(list)
                        }
                    }
                },
                {
                    if (it != State.Loading && it != State.Loaded) {
                        launchIO {
                            val list = repository.getFeedBackList(feedBacksArg.id)
                                .sortedByDescending { x -> x.id }
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
                    //setFeedBackLocal(flag, text, getCurrentDateTime().toString("dd.MM.yyyy HH:mm"))
                    if(!flag) pushNotification(
                        Pref(context).city.toString(),
                        feedBacksArg.name,
                        feedBackItem.idOrg
                    )
                } else {
                    error.postValue("Ожидайте сутки, для добавления нового отзыва")
                }
            } else {
                //setFeedBackLocal(flag, text, getCurrentDateTime().toString("dd.MM.yyyy HH:mm"))
                if(!flag) pushNotification(
                    Pref(context).city.toString(),
                    feedBacksArg.name
                )
            }

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

    private fun pushNotification(city: String, org: String, idOrg: Int = 0) = launchIO {
        firebaseApi.postNotification(city,org,idOrg,{

        }, {

        })
    }
}