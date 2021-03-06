package com.anufriev.presentation.feedBack

import android.content.ContentResolver
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
import com.anufriev.utils.ext.getCurrentDateTime
import com.anufriev.utils.ext.toString
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

    fun setFeedBack(flag: Boolean, text: String, contentResolver:ContentResolver) {
        launchIO {
            val id: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            val feedBackItem =
                repository.getLastFeedBack(feedBacksArg.id, id)
            if (feedBackItem != null) {
                if (System.currentTimeMillis() >= feedBackItem.time + 86400) {
                    setFeedBackLocal(flag, text.trim(), getCurrentDateTime().toString("dd.MM.yyyy"), id, contentResolver)
                    if(!flag) pushNotification(
                        Pref(context).city.toString(),
                        feedBacksArg.name,
                        feedBackItem.idOrg,
                        text
                    )
                } else {
                    error.postValue("Ожидайте сутки, для добавления нового отзыва")
                }
            } else {
                setFeedBackLocal(flag, text.trim(), getCurrentDateTime().toString("dd.MM.yyyy"), id, contentResolver)
                if(!flag) pushNotification(
                    Pref(context).city.toString(),
                    feedBacksArg.name,
                    0, text
                )
            }

        }
    }

    private fun setFeedBackLocal(flag: Boolean, text: String, date: String, imei:String, contentResolver:ContentResolver) {
        //Можем заносить данный отзыв
        launchIO {
            repository.setRatingReviews(
                flag, text, date,
                feedBacksArg.id, imei,
                {
                    launchIO {
                        val id: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                        repository.setFeedBackList(
                            listOf(it),
                            feedBacksArg.id,
                            id
                        )
                        val list = repository.getFeedBackList(feedBacksArg.id)
                        launch {
                            feedBacks.postValue(list)
                        }
                        val feedBackLast =
                            repository.getLastFeedBack(feedBacksArg.id, id)
                        repository.updateFeedBack(
                            FeedBackDaoEntity(
                                feedBackLast.id,
                                feedBackLast.idOrg,
                                feedBackLast.state,
                                feedBackLast.description,
                                System.currentTimeMillis(),
                                feedBackLast.uid,
                                feedBackLast.date,
                                feedBackLast.imei
                            )
                        )
                    }
                }, ::handleState
            )
        }
    }

    private fun pushNotification(city: String, org: String, idOrg: Int = 0, text:String) = launchIO {
        firebaseApi.postNotification(city,org,idOrg, text,{

        }, {

        })
    }
}