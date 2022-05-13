package com.telemedicine.indihealth.network.repo

import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.MedicalRecords
import com.telemedicine.indihealth.model.MedicalRecordsDetail
import com.telemedicine.indihealth.network.client.MedicalRecordsClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class MedicalRecordsRepository @Inject constructor(
    private val mClient: MedicalRecordsClient
) {
    suspend fun getMedicalRecords(
        id_dokter: String?,
        id_pasien: String?,
        onSuccess: (List<MedicalRecords>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getMedicalRecords(id_dokter, id_pasien) {
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
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Timber.d("jObjError $jObjError")
                        onError(jObjError.getString("msg"))
                    }catch (e : JSONException){
                        onError("Terjadi Kesalahan")
                    }
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

    suspend fun getMedicalRecordsDetail(
        id_jadwal_konsultasi: String?,
        id_pasien: String?,
        onSuccess: (MedicalRecordsDetail?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getMedicalRecordsDetail(id_jadwal_konsultasi, id_pasien) {
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
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Timber.d("jObjError $jObjError")
                        onError(jObjError.getString("msg"))
                    }catch (e : JSONException){
                        Timber.d("jObjError ${response.errorBody()?.string()}")
                        onError("Terjadi Kesalahan")
                    }
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

