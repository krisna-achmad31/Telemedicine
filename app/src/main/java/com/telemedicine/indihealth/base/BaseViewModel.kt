package com.telemedicine.indihealth.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.telemedicine.indihealth.helper.Event

abstract class BaseViewModel : ViewModel(){
    val isLoading: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    val responseStatus: MutableLiveData<Event<HashMap<String,Any?>>> by lazy {
        MutableLiveData<Event<HashMap<String,Any?>>>()
    }

    //function for change value one time
    fun setLoading(boolean: Boolean){
        isLoading.postValue(Event(boolean))
    }

    //function for change value one time
    fun setStatus(data: HashMap<String,Any?>){
        responseStatus.postValue(Event(data))
    }
}