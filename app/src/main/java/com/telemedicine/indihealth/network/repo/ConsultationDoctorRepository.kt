package com.telemedicine.indihealth.network.repo

import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.AdditionalInfo
import com.telemedicine.indihealth.model.Diagnosis
import com.telemedicine.indihealth.model.Medicine
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.network.client.ConsultationDoctorClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class ConsultationDoctorRepository @Inject constructor(
    private val client: ConsultationDoctorClient
) {

    suspend fun getScheduleDoctor(
        param: HashMap<String?,Any?>?,
        onSuccess: (List<ScheduleDoctorConsultation>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. getScheduleDoctor(param) {
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

    suspend fun postCallVideo(
        param: HashMap<String,String?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. postCallVideo(param) {
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

    suspend fun postChatImage(
        param: HashMap<String?, RequestBody?>,
        file: MultipartBody.Part?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. postChatImage(param,file) {
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

    suspend fun getMedicine(
        param: HashMap<String?,Any?>?,
        onSuccess: (List<Medicine>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. getMedicine(param) {
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

    suspend fun clearPrescription(
//        param: String?,
        param: HashMap<String,String?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. clearPrescription(param) {
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


    suspend fun postPrescription(
        param: HashMap<String,String?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. postPrescription(param) {
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

    suspend fun getDiagnosis(
        param: HashMap<String?,Any?>?,
        onSuccess: (List<Diagnosis>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. getDiagnosis(param) {
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

    suspend fun postDiagnosis(
        param: HashMap<String,String?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. postDiagnosis(param) {
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

    suspend fun postRejectCall(
        param: HashMap<String,String?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. postRejectCall(param) {
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

    suspend fun postAnswerCall(
        param: HashMap<String,String?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. postAnswerCall(param) {
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

    suspend fun postEndCall(
        param: HashMap<String,String?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. postEndCall(param) {
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

    suspend fun postEndCallByPasien(
        param: HashMap<String,String?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. postEndCallByPasien(param) {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        Timber.d("success = $data")
                        onSuccess(data?.msg)
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

    suspend fun postEndCallByDokter(
        param: HashMap<String,String?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client. postEndCallByDokter(param) {
            Timber.d("responseEndCall .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        Timber.d("success = $data")
                        onSuccess(data?.msg)
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

    suspend fun postAdditionalInfo(
        param: HashMap<String,String>,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client.postAdditional(param) {
            Timber.d("postAdditionalInfo .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        Timber.d("success = $data")
                        onSuccess(data?.msg)
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

    suspend fun getAdditionalInfo(
        param: HashMap<String, String?>?,
        onSuccess: (AdditionalInfo?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        client.getAdditional(param) {
            Timber.d("postAdditionalInfo .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        Timber.d("success = $data")
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
}

