package com.anufriev.presentation.resultCall

import android.util.Log
import androidx.room.withTransaction
import com.anufriev.data.db.CityDatabase
import com.anufriev.data.db.entities.PhoneCallDaoEntity
import com.anufriev.data.repository.OrganizationRepository
import com.anufriev.data.repository.PhoneCallRepository
import com.anufriev.utils.platform.BaseViewModel

class ResultCallViewModel(
    private val repository: OrganizationRepository,
    private val local: PhoneCallRepository
) : BaseViewModel() {

    private fun setRating(flag: Boolean, id: Int) {
        launchIO {
            repository.setRating(id, flag, {
                Log.d("Rating", "Рейтинг организации успешно изменён")
            }, ::handleState)
        }
    }

    fun getRatingPhoneCall(flag: Boolean, idOrg: Int, uid: String) {
        launchIO {
            val result = local.getPhoneCallLast(uid, idOrg)
            if (result != null) {
                //Сравниваем по времени
                if (System.currentTimeMillis() >= result.time + 86400)
                {
                    setRatingPhoneCall(
                        System.currentTimeMillis(),
                        uid, idOrg
                    )
                    setRating(flag, idOrg)
                }
            } else {
                //добавляем отзыв
                setRatingPhoneCall(
                    System.currentTimeMillis(),
                    uid, idOrg
                )
                setRating(flag, idOrg)
            }
        }
    }

    private fun setRatingPhoneCall(time: Long, uid: String, idOrg: Int) {
        launchIO {
            CityDatabase.instance.withTransaction {
                local.setPhoneCallList(
                    listOf(
                        PhoneCallDaoEntity(
                            0, time, uid, idOrg
                        )
                    )
                )
            }
        }
    }
}