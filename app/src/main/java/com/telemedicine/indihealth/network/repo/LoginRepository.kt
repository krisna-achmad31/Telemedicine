package com.telemedicine.indihealth.network.repo

import androidx.lifecycle.MutableLiveData
import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.client.LoginClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginClient: LoginClient
) {

    suspend fun getLogin(
        email: String,
        password: String,
        onSuccess: (User?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<User>()
        loginClient.getLogin(email, password) {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        liveData.postValue(data?.data)
                        onSuccess(data?.data)
                    } else {
                        Timber.d("is error = ${data?.message}")
                        onError(data?.message)
                    }
                }
            }
                .onError {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Timber.d("onError")
                    onError(jObjError.getString("msg"))
                }
                .onException {
                    Timber.d("onException ${message()}" )
                    onError("Terjadi Kesalahan Pada Jaringan")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }
}