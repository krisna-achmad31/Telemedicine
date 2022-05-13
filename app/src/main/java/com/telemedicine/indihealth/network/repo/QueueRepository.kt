package com.telemedicine.indihealth.network.repo

import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.Queue
import com.telemedicine.indihealth.network.client.QueueClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


class QueueRepository @Inject constructor(
    private val mClient: QueueClient
) {

    suspend fun getQueue(
        id_pasien: String?,
        status: String,
        onSuccess: (List<Queue>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getQueue(id_pasien, status) {
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
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Timber.d("jObjError $jObjError")
                    onError(jObjError.getString("msg"))
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

