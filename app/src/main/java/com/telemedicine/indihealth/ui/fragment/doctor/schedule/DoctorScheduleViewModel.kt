package com.telemedicine.indihealth.ui.fragment.doctor.schedule

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.DoctorSchedule
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.DoctorScheduleRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class DoctorScheduleViewModel @ViewModelInject constructor(
    private val mRepository: DoctorScheduleRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

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

    private val user: User? by lazy {
        sharedPreference.getUserValue()
    }

    val queueList: MutableLiveData<List<DoctorSchedule>> by lazy {
        MutableLiveData<List<DoctorSchedule>>()
    }

    private fun setQueueListAsync(it: List<DoctorSchedule>?) {
        queueList.postValue(it)
    }

    fun getQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getDoctorSchedule(
                id_dokter = sharedPreference.getUserValue()?.id,
                onSuccess = {
                    setLoading(false)
                    setQueueListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setQueueListAsync(listOf())
                }
            )
        }
    }

    fun initQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getDoctorSchedule(
                id_dokter = sharedPreference.getUserValue()?.id,
                onSuccess = {
                    setLoading(false)
                    setQueueListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setQueueListAsync(listOf())
                }
            )
        }
    }
}