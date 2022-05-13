package com.telemedicine.indihealth.network.repo

import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.client.TocClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class TocRepository @Inject constructor(
    private val mClient: TocClient
) {
    suspend fun getPatientProfile(
        userId: String?,
        stringPassword: String?,
        onSuccess: (User?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getPatientProfile(userId,stringPassword) {
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
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Timber.d("jObjError $jObjError")
                    onError(jObjError.getString("msg"))
                }
                .onException {
                    Timber.d("onException")
                    onError("Exception")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun postUpdateToc(
        userId: String?,
        onSuccess: (Boolean?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.postUpdateToc(userId) {
            it
                .onSuccess {
                    if (data != null) {
                        if (data?.status!!) {
                            onSuccess(true)
                        } else {
                            Timber.d("is error = ${data?.message}")
                            onError(data?.message)
                        }
                    }
                }
                .onError {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Timber.d("jObjError $jObjError")
                    onError(jObjError.getString("msg"))
                }
                .onException {
                    Timber.d("onException")
                    onError("Eception")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun getToc(
        userId: String,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getToc(userId) {
            it
                .onSuccess {
                    if (data != null) {
                        onSuccess(data!!.data)
                    }
                }
                .onError {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Timber.d("jObjError $jObjError")
                    onError(jObjError.getString("msg"))
                }
                .onException {
                    Timber.d("onException")
                    onError("Eception")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }
}