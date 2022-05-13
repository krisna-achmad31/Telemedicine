package com.telemedicine.indihealth.network.repo

import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.News
import com.telemedicine.indihealth.model.Notification
import com.telemedicine.indihealth.network.client.MainClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainClient: MainClient
) {

    suspend fun getNews(
        onSuccess: (List<News>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mainClient.getNews {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        onSuccess(data?.data)
                    } else {
                        Timber.d("is error = ${data?.message}")
                        onError(data?.message)
                    }
                }
            }
                .onError {
                    Timber.d("onError")
                    onError(message())
                }
                .onException {
                    Timber.d("onException")
                    onError(message())
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun getNotification(
        hashMap: HashMap<String?, String?>,
        onSuccess: (List<Notification>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mainClient.getNotification(hashMap) {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        onSuccess(data?.data)
                    } else {
                        Timber.d("is error = ${data?.msg}")
                        onError(data?.msg)
                    }
                }
            }
                .onError {
                    Timber.d("onError")
                    onError(message())
                }
                .onException {
                    Timber.d("onException")
                    onError(message())
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun postNotificationReadAll(
        hashMap: HashMap<String?, String?>,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mainClient.postNotificationReadAll(hashMap) {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        onSuccess(data?.msg)
                    } else {
                        Timber.d("is error = ${data?.msg}")
                        onError(data?.msg)
                    }
                }
            }
                .onError {
                    Timber.d("onError")
                    onError(message())
                }
                .onException {
                    Timber.d("onException")
                    onError(message())
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }
}

