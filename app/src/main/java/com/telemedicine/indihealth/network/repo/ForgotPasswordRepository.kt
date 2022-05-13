package com.telemedicine.indihealth.network.repo

import androidx.lifecycle.MutableLiveData
import com.skydoves.sandwich.*
import com.telemedicine.indihealth.network.client.ForgotPasswordClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class ForgotPasswordRepository @Inject constructor(
    private val forgotPasswordClient: ForgotPasswordClient
) {

    suspend fun forgotPassword(
        email: String,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<String>()
        forgotPasswordClient.forgotPassword(email) {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        liveData.postValue(data?.message)
                        onSuccess(data?.message)
                    } else {
                        Timber.d("is error = ${data?.message}")
                        onError(data?.message)
                    }
                }
            }
                .onError {
                    Timber.d("onError")
                    onError("Email yang anda masukan tidak terdaftar")
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

