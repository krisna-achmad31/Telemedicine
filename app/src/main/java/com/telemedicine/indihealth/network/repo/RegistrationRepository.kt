package com.telemedicine.indihealth.network.repo

import androidx.lifecycle.MutableLiveData
import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.client.RegistrationClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class RegistrationRepository @Inject constructor(
    private val registrationClient: RegistrationClient
) {

    suspend fun postRegistration(
        password: String,
        email: String,
        name: String,
        lahir_tanggal: String,
        id_user_kategori: String,
        aktif: String,
        username: String,
        lahir_tempat: String,
        jenis_kelamin: String,
        telephone: String,
        onSuccess: (User?) -> Unit,
        onError: (String?) -> Unit,
        onResult:(String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<User>()
        registrationClient.postRegistration(password, email, name, lahir_tanggal, id_user_kategori, aktif, username, lahir_tempat, jenis_kelamin, telephone) {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        liveData.postValue(data?.data)
                        onSuccess(data?.data)
                        onResult(data?.message)
                    } else {
                        Timber.d("is error = ${data?.message}")
                        onError(data?.message)
                    }
                }
            }
                .onError {
                    Timber.d("onError : %s", message())
                    onError("Terjadi kesalahan.")
                }
                .onException {
                    Timber.d("onException: %s", message())
                    onError("Terjadi kesalahan.")
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

}

