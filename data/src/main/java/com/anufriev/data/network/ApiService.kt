package com.anufriev.data.network

import com.anufriev.data.entity.FeedBack
import com.anufriev.data.entity.Organization
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    //Получить список организаций
    @GET("api/organizations")
    suspend fun getOrg():List<Organization>

    //Установить рейтинг после звонка (true(+1)|false(-1))
    @POST("api/organizations/{id}")
    suspend fun setRating(
        @Path("id") id:Int,
        @Body body: RequestBody
    )

    //Получить список отзывов по конкретной организации
    @GET("api/organizations/{id}/reviews")
    suspend fun getReviews(@Path("id") id:Int):List<FeedBack>

    //Добавить отзыв
    @POST("api/organizations/{id}/reviews")
    suspend fun setRatingReviews(
        @Path("id") id:Int,
        @Body body: RequestBody
    ):FeedBack
}
