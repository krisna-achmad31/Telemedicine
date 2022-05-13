package com.telemedicine.indihealth.ui.fragment.profile.updatepassword

import android.text.Editable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.network.repo.ProfileRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class UpdatePasswordViewModel @ViewModelInject constructor(
    private val mRepository: ProfileRepository,
    private val sharedPreference: SharedPreferenceApp
): BaseViewModel(){

    val newPassword: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val oldPassword: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val confirmNewPassword: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isFieldsFilled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isUpdateSuccess: MutableLiveData<Event<String>> by lazy {
        MutableLiveData<Event<String>>()
    }

    @Suppress("UNUSED_PARAMETER")
    fun afterTextChanged(e: Editable) {
        isFieldsFilled.value =
            !(newPassword.value.isNullOrBlank() || oldPassword.value.isNullOrBlank() || confirmNewPassword.value.isNullOrBlank())
        Timber.d("isFieldFilled? = ${isFieldsFilled.value}")
    }

    fun attemptUpdatePassword() {
        if (newPassword.value == confirmNewPassword.value){
            setLoading(true)
            viewModelScope.launch(Dispatchers.IO) {
                mRepository.postUpdatePassword(
                    userId = sharedPreference.getUserValue()!!.id!!,
                    newPassword = newPassword.value!!,
                    oldPassword = oldPassword.value!!,
                    onResult = {
                        setLoading(false)
                        if (it.equals("Success",true)){
                            isUpdateSuccess.postValue(Event(it!!))
                        }else{
                            isUpdateSuccess.postValue(Event(it!!))
                        }
                    }
                )
            }
        }else{
            isUpdateSuccess.postValue(Event("Password baru yang Anda masukan tidak sama!"))
        }
    }

    fun resetPassword(){
        oldPassword.postValue("")
        newPassword.postValue("")
        confirmNewPassword.postValue("")
    }
}