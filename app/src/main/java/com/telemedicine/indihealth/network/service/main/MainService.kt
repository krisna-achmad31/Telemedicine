package com.telemedicine.indihealth.network.service.main

import com.skydoves.sandwich.DataSource
import com.telemedicine.indihealth.model.GlobalResponse
import retrofit2.http.*

interface MainService {

    @GET("all/news/list")
    suspend fun getNews(): DataSource<NewsResponse>

    @GET("Notifikasi/list")
    suspend fun getNotification(
        @QueryMap condition: HashMap<String?, String?>
    ): DataSource<NotificationResponse>


    @FormUrlEncoded
    @POST("Notifikasi/bacaSemua")
    suspend fun postNotificationReadAll(
        @FieldMap condition: HashMap<String?, String?>
    ): DataSource<GlobalResponse>
}