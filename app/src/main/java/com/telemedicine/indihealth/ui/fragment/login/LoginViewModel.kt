package com.telemedicine.indihealth.ui.fragment.login

import android.text.Editable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.network.repo.LoginRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel @ViewModelInject constructor(
    private val loginRepository: LoginRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {


    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isFieldsFilled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isLoggedIn: MutableLiveData<Event<String>> by lazy {
        MutableLiveData<Event<String>>().apply {
            Timber.d("isLoggedIn = ${sharedPreference.getLoggedInStatus()}")
            if (sharedPreference.getLoggedInStatus()) {
                Timber.d("isLoggedIn = success${sharedPreference.getUserValue()?.role}")
                value = Event("success${sharedPreference.getUserValue()?.role}")
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun afterTextChanged(e: Editable) {
        isFieldsFilled.value =
            !(email.value.isNullOrBlank() || password.value.isNullOrBlank())
        Timber.d("isFieldFilled? = ${isFieldsFilled.value}")
    }

    fun attemptLogin(
        onError: (String) -> Unit,
        onSuccess: (String) -> Unit
    ) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.getLogin(
                email = email.value!!,
                password = password.value!!,
                onSuccess = {
                    setLoading(false)
                    sharedPreference.setUserValue(it)
                    setLoading(false)
                    onSuccess("success${it!!.role}")
                    sharedPreference.setLoggedInStatus(true)
                    Timber.d("User = ${sharedPreference.getUserValue()?.username}")
                },
                onError = {
                    setLoading(false)
                    onError(it!!)
                }
            )
        }
    }

    fun setLogOutStatus(){
        sharedPreference.setLogOutStatus(false)
    }

    fun setBoardingStatus(){
        sharedPreference.setBoardingStatus(true)
    }
}