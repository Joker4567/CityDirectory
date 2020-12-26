package com.anufriev.data.network

import com.anufriev.data.entity.FeedBack
import com.anufriev.data.entity.GeoCity
import com.anufriev.data.entity.Organization
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //Получить список организаций
    @GET("api/organizations/map/{city}")
    suspend fun getOrg(@Path("city") city:String):List<Organization>

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

    @POST()
    @Headers(
        "Authorization: Token f4fb9e447f9852a1f65edc1e8513aa988f4251c9",
        "Content-Type: application/json")
    suspend fun getCity(
        @Url url:String,
        @Body body: RequestBody
    ): GeoCity
}
