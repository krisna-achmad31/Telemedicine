package com.telemedicine.indihealth.ui.fragment.profile.updateusername

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

class UpdateUsernameViewModel @ViewModelInject constructor(
    private val mRepository: ProfileRepository,
    private val sharedPreference: SharedPreferenceApp
): BaseViewModel(){

    val username: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isFieldsFilled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isUpdateSuccess: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    @Suppress("UNUSED_PARAMETER")
    fun afterTextChanged(e: Editable) {
        isFieldsFilled.value =
            !(username.value.isNullOrBlank() || password.value.isNullOrBlank())
        Timber.d("isFieldFilled? = ${isFieldsFilled.value}")
    }

    fun attemptUpdateUsername() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postUpdateUsername(
                userId = sharedPreference.getUserValue()!!.id!!,
                username = username.value!!,
                password = password.value!!,
                onResult = {
                    setLoading(false)
                    if (it.equals("Success",true)){
                        val user = sharedPreference.getUserValue()
                        user?.username = username.value
                        sharedPreference.setUserValue(user)
                        isUpdateSuccess.postValue(Event(true))
                    }else{
                        isUpdateSuccess.postValue(Event(false))
                    }
                }
            )
        }
    }

    fun resetPassword(){
        password.postValue("")
    }

    fun getUsernameFromsharedPreference(){
        username.postValue(sharedPreference.getUserValue()!!.username)
        Timber.d("username = ${username.value}")
    }
}
