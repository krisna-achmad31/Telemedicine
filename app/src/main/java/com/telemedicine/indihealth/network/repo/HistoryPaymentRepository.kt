package com.telemedicine.indihealth.network.repo

import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.HistoryConsultation
import com.telemedicine.indihealth.model.HistoryDrug
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.client.HistoryPaymentClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


class HistoryPaymentRepository @Inject constructor(
    private val mClient: HistoryPaymentClient
) {

    suspend fun getDoctorProfile(
        hashMap: HashMap<String?, String?>,
        onSuccess: (User?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getDoctorProfile(hashMap) {
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

    suspend fun getHistoryPayment(
        id_pasien: String?,
        onSuccess: (List<HistoryConsultation>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getHistoryPayment(id_pasien) {
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

    suspend fun getHistoryPaymentDrug(
        id_pasien: String?,
        onSuccess: (List<HistoryDrug>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getHistoryPaymentDrug(id_pasien) {
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

}

