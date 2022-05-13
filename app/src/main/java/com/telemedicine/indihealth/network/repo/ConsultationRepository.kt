package com.telemedicine.indihealth.network.repo

import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.*
import com.telemedicine.indihealth.network.client.ConsultationClient
import com.telemedicine.indihealth.network.service.consultation.asessment.AssessmentListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


class ConsultationRepository @Inject constructor(
    private val mClient: ConsultationClient
) {

    suspend fun getConsultationDoctor(
        hashMap: HashMap<String, Any>,
        onSuccess: (List<ConsultationDoctor>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getConsultationDoctor(hashMap) {
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

    suspend fun postRegistrationDoctor(
        hashMap: HashMap<String, Any?>,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient. postRegistrationDoctor(hashMap) {
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

    suspend fun getPaymentList(
        hashMap: HashMap<String?, String?>,
        onSuccess: (List<Payment>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient. getPaymentList(hashMap) {
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

    suspend fun getBankTransferList(
        onSuccess: (List<ListPaymetnMethod>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getBankTransferList() {
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

    suspend fun postPayment(
        param: HashMap<String?,RequestBody?>,
        foto: MultipartBody.Part?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient. postPayment(param,foto) {
            Timber.d("response .... $it")
            val onFailure = it.onSuccess {
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
                    Timber.d("onException ${message()}" )
                    onError("Terjadi kesalahan")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }



    suspend fun updateCanceledOnProcessPayment(
        param: HashMap<String?,Any?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient. updateCanceledOnProcessPayment(param) {
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
                    Timber.d("onException ${message()}" )
                    onError("Terjadi kesalahan")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun deletePayment(
        param: HashMap<String?,Any?>?,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient. deletePayment(param) {
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
                    Timber.d("onException ${message()}" )
                    onError("Terjadi kesalahan")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun getAssessment(
        param: HashMap<String?,Any?>?,
        onSuccess: (Assessment?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient. getAssessment(param) {
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
                    Timber.d("onException ${message()}" )
                    onError("Terjadi kesalahan")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun postAssessment(
        param: HashMap<String?,RequestBody?>?,
        foto: List<MultipartBody.Part>,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient. postAssessment(param, foto) {
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
                    Timber.d("onException ${message()}" )
                    onError("Terjadi kesalahan")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun updateAssessment(
        param: HashMap<String?, RequestBody?>?,
        foto: List<MultipartBody.Part>,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient. updateAssessment(param, foto) {
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
                    Timber.d("onException ${message()}" )
                    onError("Terjadi kesalahan")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun getSchedule(
        param: HashMap<String?,Any?>?,
        onSuccess: (List<Schedule>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient. getSchedule(param) {
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
                    Timber.d("onException ${message()}" )
                    onError("Terjadi kesalahan")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun postPaymentOwlexaGenerateOtp(
        param: HashMap<String?,String?>,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.postOwlexaGenerateOtp(param) {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status.equals("SUCCESS")) {
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

    suspend fun postPaymentOwlexaVerification(
        param: HashMap<String?,String?>,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.postOwlexaVerification(param) {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status.equals("SUCCESS")) {
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

    suspend fun getTocOwlexa(
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    )= withContext(Dispatchers.IO){
        mClient
            .getTocOwlexa {
                it.onSuccess {
                    if (data!=null){
                        if(data?.code==200){
                            var toc : String = ""
                            for (item in data?.data!!){
                                toc += "$item\n"
                            }
                            onSuccess(toc)
                            Timber.d("TOC $toc")
                        }else{
                            onError(data?.msg)
                        }
                    }else{
                        onError("Null Response")
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

    suspend fun getAssessmentList(
        patientId : String,
        onSuccess: (AssessmentListResponse?) -> Unit,
        onError: (String?) -> Unit
    )= withContext(Dispatchers.IO){
        mClient
            .getAssessmentList(patientId) {
                it.onSuccess {
                    if (data!=null){
                        if(data?.status!!){
                            onSuccess(data)
                            Timber.d("Assessment List ${data?.data}")
                        }else{
                            onError(data?.msg)
                        }
                    }else{
                        onError("Null Response")
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

    suspend fun getVaList(
        onSuccess: (List<ListPaymetnMethod>?) -> Unit,
        onError: (String?) -> Unit
    )= withContext(Dispatchers.IO){
        mClient
            .getVaList() {
                it.onSuccess {
                    if (data!=null){
                        if(data?.status!!){
                            onSuccess(data?.data)
                            Timber.d("Virtual Account List ${data.toString()}")
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

    suspend fun getProfileDoctor(
        hashMap: HashMap<String, Any>,
        onSuccess: (User?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getProfileDoctor(hashMap) {
            Timber.d("response .... $it")
            it
                .onSuccess {
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
                    Timber.d("onException $message")
                    onError(message())
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }
}

