package com.telemedicine.indihealth.ui.fragment.forgotpassword

import android.text.Editable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.network.repo.ForgotPasswordRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ForgotPasswordViewModel @ViewModelInject constructor(
    private val forgotPasswordRepository: ForgotPasswordRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {


    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isFieldsFilled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val message: MutableLiveData<Event<String>> by lazy {
        MutableLiveData<Event<String>>()
    }

    val isSuccess: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    private fun setMessage(string: String) {
        message.postValue(Event(string))
    }
    private fun setSuccess(boolean: Boolean) {
        isSuccess.postValue(Event(boolean))
    }

    @Suppress("UNUSED_PARAMETER")
    fun afterTextChanged(e: Editable) {
        isFieldsFilled.value =
            !(email.value.isNullOrBlank())
        Timber.d("isFieldFilled? = ${isFieldsFilled.value}")
    }

    fun attemptForgotPassword() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            forgotPasswordRepository.forgotPassword(
                email = email.value!!,
                onSuccess = {
                    setLoading(false)
                    it?.let { it1 -> setMessage(it1) }
                    setSuccess(true)
                    Timber.d("User = ${sharedPreference.getUserValue()}")
                },
                onError = {
                    setLoading(false)
                    setSuccess(false)
                    it?.let { it1 -> setMessage(it1) }
                }
            )
        }
    }
}