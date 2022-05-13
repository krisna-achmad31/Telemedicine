package com.telemedicine.indihealth.network.repo

import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.ConsultationDoctor
import com.telemedicine.indihealth.network.client.PharmacyClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Surya Mahadi on 11/23/2021
 */
class PharmacyRepository @Inject constructor(
    private val mClient: PharmacyClient
) {

    suspend fun postAnswerCall(
        hashMap: HashMap<String, String?>,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.postAnswerCall(hashMap) {
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

    suspend fun postRejectCall(
        hashMap: HashMap<String, String?>,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.postRejectCall(hashMap) {
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