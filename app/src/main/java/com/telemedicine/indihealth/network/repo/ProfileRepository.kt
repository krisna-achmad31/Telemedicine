package com.telemedicine.indihealth.network.repo

import androidx.lifecycle.MutableLiveData
import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.GlobalItem
import com.telemedicine.indihealth.model.ProfileDoctor
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.client.ProfileClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


class ProfileRepository @Inject constructor(
    private val mClient: ProfileClient
) {

    suspend fun getPatientProfile(
        hashMap: HashMap<String?, String?>,
        onSuccess: (User?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getPatientProfile(hashMap) {
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
                    onError(message())
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

    suspend fun postEditProfile(
        hashMap: HashMap<String, RequestBody?>,
        foto: MultipartBody.Part?,
        onSuccess: (User?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<User>()
        mClient.postEditProfile(hashMap, foto) {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        if(data?.data != null){
                            liveData.postValue(data?.data)
                            onSuccess(data?.data)
                        }else {
                            onError(data?.message)
                        }
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
                    Timber.d("onException $message ${message()}")
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
        onSuccess: (List<ProfileDoctor>?) -> Unit,
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

    suspend fun getProvinsi(
        hashMap: HashMap<String, String?>,
        onSuccess: (List<GlobalItem>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getProvinsi(hashMap) { response ->
            Timber.d("response .... $response")
            response
                .onSuccess {
                    data?.let {
                        onSuccess(it.data)
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

    suspend fun getKota(
        hashMap: HashMap<String, String?>,
        onSuccess: (List<GlobalItem>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getKota(hashMap) { response ->
            Timber.d("response .... $response")
            response
                .onSuccess {
                    data?.let {
                        onSuccess(it.data)
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

    suspend fun getKecamatan(
        hashMap: HashMap<String, String?>,
        onSuccess: (List<GlobalItem>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getKecamatan(hashMap) { response ->
            Timber.d("response .... $response")
            response
                .onSuccess {
                    data?.let {
                        onSuccess(it.data)
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

    suspend fun getKelurahan(
        hashMap: HashMap<String, String?>,
        onSuccess: (List<GlobalItem>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getKelurahan(hashMap) { response ->
            Timber.d("response .... $response")
            response
                .onSuccess {
                    data?.let {
                        onSuccess(it.data)
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

    suspend fun postUpdateUsername(
        userId: String,
        username: String,
        password: String,
        onResult: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.postUpdateUsername(
            userId,
            username,
            password
        ) {
            it.onSuccess {
                if (data != null) {
                    Timber.d("isUpdateUsernameSuccess = ${data?.message}")
                    if (data?.status!!) {
                        onResult("Success")
                    } else {
                        onResult(data?.message)
                    }
                } else {
                    onResult("Null Response")
                }
            }
                .onError {
                    Timber.d("onError")
                    onResult("onError")
                }
                .onException {
                    Timber.d("onException")
                    onResult("onException")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onResult("onFailure")
                }
        }
    }

    suspend fun postUpdatePassword(
        userId: String,
        newPassword: String,
        oldPassword: String,
        onResult: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.postUpdatePassword(
            userId,
            newPassword,
            oldPassword
        ) {
            it.onSuccess {
                if (data != null) {
                    Timber.d("isUpdateSuccessPassword = ${data?.message}")
                    if (data?.status!!) {
                        onResult("Success")
                    } else {
                        onResult(data?.message)
                    }
                } else {
                    onResult("Null Response")
                }
            }
                .onError {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Timber.d("jObjError $jObjError")
                    onResult(jObjError.getString("message"))
                }
                .onException {
                    Timber.d("onException")
                    onResult("onException")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onResult("onFailure")
                }
        }
    }
}

