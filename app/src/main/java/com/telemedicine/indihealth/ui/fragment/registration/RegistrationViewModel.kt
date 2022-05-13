package com.telemedicine.indihealth.ui.fragment.registration

import android.text.Editable
import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.network.repo.RegistrationRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RegistrationViewModel @ViewModelInject constructor(
    private val registerRepository: RegistrationRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {


    val fullname: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val username: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val telephone: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val confirm_password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val birth_place: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val birth_date: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val gender: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isFieldsFilled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val toastMessage: MutableLiveData<Event<String>> by lazy {
        MutableLiveData<Event<String>>()
    }

    private fun setToastMessage(string: String) {
        toastMessage.postValue(Event(string))
    }

    @Suppress("UNUSED_PARAMETER")
    fun afterTextChanged(e: Editable) {
        isFieldsFilled.value =
            !(fullname.value.isNullOrBlank() || username.value.isNullOrBlank() ||
                    telephone.value.isNullOrBlank() || email.value.isNullOrBlank() ||
                    password.value.isNullOrBlank() || confirm_password.value.isNullOrBlank() ||
                    birth_place.value.isNullOrBlank() || birth_date.value.isNullOrBlank() ||
                    gender.value.isNullOrBlank())
        Timber.d("isFieldFilled? = ${isFieldsFilled.value}")
    }

    fun attemptRegister() {
        if(validation()){
            if (password.value == confirm_password.value) {
                setLoading(true)
                val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val inputDateStr = birth_date.value
                val date: Date = inputFormat.parse(inputDateStr)
                val outputDateStr: String = outputFormat.format(date)

                viewModelScope.launch(Dispatchers.IO) {
                    registerRepository.postRegistration(
                        password = password.value!!,
                        email = email.value!!,
                        name = fullname.value!!,
                        lahir_tanggal = outputDateStr,
                        id_user_kategori = "0",
                        aktif = "1",
                        username = username.value!!,
                        lahir_tempat = birth_place.value!!,
                        jenis_kelamin = gender.value!!,
                        telephone = telephone.value!!,
                        onSuccess = {
                            setLoading(false)
                            sharedPreference.setUserValue(it)
                            Timber.d("User = ${sharedPreference.getUserValue()}")
                        },
                        onError = {
                            setLoading(false)
                            setStatus(hashMapOf(
                                "status" to "error",
                                "message" to it
                            ))
                        },
                        onResult = {
                            setStatus(hashMapOf(
                                "status" to "success",
                                "message" to it
                            ))
                        }
                    )
                }
            } else {
                setToastMessage("password yang dimasukkan tidak sama")
            }
        }
    }

    private fun validation(): Boolean {
        var result = true
        val mEmail = email.value
        if (!mEmail.isNullOrEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                setToastMessage("email tidak valid")
                result = false
            }
        } else {
            setToastMessage("email harus diisi")
            result = false
        }

        val mPhone = telephone.value

        if (!mPhone.isNullOrEmpty()) {
            if (mPhone.length < 10) {
                setToastMessage("Nomor telepon harus lebih dari 10 karakter")
                result = false
            } else if (mPhone.length > 13) {
                setToastMessage("Nomor telepon maksimal 13 karakter")
                result = false
            }
        } else {
            setToastMessage("Nomor telepon harus diisi")
            result = false
        }

        val mPassword = password.value
        if(!mPassword.isNullOrEmpty()){
            if(mPassword.length < 8){
                setToastMessage("Password harus lebih dari 8 karakter")
                result = false
            }
        }
        return result
    }
}