package com.telemedicine.indihealth.ui.fragment.toc

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.network.repo.TocRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class TocViewModel @ViewModelInject constructor(
    private val mRepository: TocRepository,
    private val sharedPreference: SharedPreferenceApp
): BaseViewModel() {

    val toc = MutableLiveData<String>()

    init {
        setLoading(true)
        viewModelScope.launch {
            mRepository.getToc(sharedPreference.getUserValue()!!.id!!,
                onSuccess = {
                    setLoading(false)
                    toc.postValue(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                }
            )
        }
    }

    fun getStringHtml(): String{
        return sharedPreference.getUserValue()?.toc!!
    }

    fun postUpdateToc(
        onResult: (String) -> Unit
    ) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postUpdateToc(
                sharedPreference.getUserValue()?.id,
                onSuccess = {
                    setLoading(false)
                    sharedPreference.setLoggedInStatus(true)
                    Timber.d("User postUpdateToc = ${sharedPreference.getUserValue()?.role}")
                    onResult("success")
                },
                onError = {
                    setLoading(false)
                    onResult(it!!)
                    Timber.d("error = $it")
                }
            )
        }
    }
}