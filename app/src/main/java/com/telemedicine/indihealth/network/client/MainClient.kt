package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.model.GlobalResponse
import com.telemedicine.indihealth.network.service.main.MainService
import com.telemedicine.indihealth.network.service.main.NewsResponse
import com.telemedicine.indihealth.network.service.main.NotificationResponse
import javax.inject.Inject

class MainClient @Inject constructor(
    private val mainService: MainService
) {
    suspend fun getNews(
        onResult: (response: ApiResponse<NewsResponse>) -> Unit
    ) {
        mainService
            .getNews()
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getNotification(
        hashMap: HashMap<String?, String?>,
        onResult: (response: ApiResponse<NotificationResponse>) -> Unit
    ) {
        mainService
            .getNotification(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }


    suspend fun postNotificationReadAll(
        hashMap: HashMap<String?, String?>,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        mainService
            .postNotificationReadAll(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }
}