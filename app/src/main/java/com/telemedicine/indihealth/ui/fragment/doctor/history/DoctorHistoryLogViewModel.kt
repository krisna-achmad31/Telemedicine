package com.telemedicine.indihealth.ui.fragment.doctor.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.DoctorHistoryLog
import com.telemedicine.indihealth.network.repo.DoctorHistoryLogRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class DoctorHistoryLogViewModel @ViewModelInject constructor(
    private val mRepository: DoctorHistoryLogRepository,
    private val sharedPreference: SharedPreferenceApp

) : BaseViewModel() {

    private val TAG ="com.telemedicine.indihealth.ui.fragment.doctor.history.DoctorHistoryLogViewModel"

    val isQueueListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    fun setQueueListExist(boolean: Boolean?) {
        isQueueListExist.value = boolean
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }

    val queueList: MutableLiveData<List<DoctorHistoryLog>> by lazy {
        MutableLiveData<List<DoctorHistoryLog>>()
    }

    var activity: String? = ""

    private fun setQueueListAsync(it: List<DoctorHistoryLog>?) {
        var temp: List<DoctorHistoryLog> = mutableListOf()
        for (histori in it!!){
            if (histori.activity == activity) {
                temp += histori
            }
        }
        Timber.d(("$TAG history: $it"))
        queueList.postValue(temp)
    }

    fun getQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getHistoryLog(
                id_dokter = sharedPreference.getUserValue()?.id,
                onSuccess = {
                    Timber.d(("$TAG history: $it"))
                    setQueueListAsync(it)
                    setLoading(false)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setQueueListAsync(listOf())
                }
            )
        }
        setLoading(false)
    }

    fun initQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getHistoryLog(
                id_dokter = sharedPreference.getUserValue()?.id,
                onSuccess = {
                    setQueueListAsync(it)
                    Timber.d(("$TAG history: $it"))
                    setLoading(false)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setQueueListAsync(listOf())
                }
            )
        }
        setLoading(false)
    }
}