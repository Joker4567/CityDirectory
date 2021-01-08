package com.anufriev.data.network

import com.anufriev.data.entity.FeedBack
import com.anufriev.data.entity.GeoCity
import com.anufriev.data.entity.Organization
import com.anufriev.utils.services.CloudMessage
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

    //Developer API eb78da4a7d0bb18dedb3eeadc9c144ca9f8e65ce
    //Production API f4fb9e447f9852a1f65edc1e8513aa988f4251c9
    @POST()
    @Headers(
        "Authorization: Token eb78da4a7d0bb18dedb3eeadc9c144ca9f8e65ce",
        "Content-Type:${CloudMessage.CONTENT_TYPE}"
    )
    suspend fun getCity(
        @Url url:String,
        @Body body: RequestBody
    ): GeoCity
}
