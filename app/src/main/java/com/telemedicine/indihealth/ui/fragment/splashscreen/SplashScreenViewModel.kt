package com.telemedicine.indihealth.ui.fragment.splashscreen

import androidx.hilt.lifecycle.ViewModelInject
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.helper.SingleLiveData
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import timber.log.Timber

class SplashScreenViewModel @ViewModelInject constructor(
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    val isLoggedIn: SingleLiveData<Event<String>> by lazy {
        SingleLiveData<Event<String>>().apply {
            Timber.d("isLoggedIn = ${sharedPreference.getLoggedInStatus()}")
            if (sharedPreference.getLoggedInStatus()) {
                Timber.d("isLoggedIn = success${sharedPreference.getUserValue()?.role}")
                value = Event("success${sharedPreference.getUserValue()?.role}")
            }
        }
    }



    fun getLogInStatus(): String {
        sharedPreference.clearCallNotif()
        return (
                if (sharedPreference.getLoggedInStatus()) {
                    "success${sharedPreference.getUserValue()?.role}"
                } else {
                    "NO_DATA"
                }
                ).toString()
    }

    fun getBoardingStatus():Boolean{
        return sharedPreference.getBoardingStatus()
    }

    fun getLogOutStatus(): Boolean{
        return sharedPreference.getLogOutStatus()
    }
}