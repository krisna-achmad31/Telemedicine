package com.telemedicine.indihealth.ui.fragment.consultation.schedule

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.Schedule
import com.telemedicine.indihealth.network.repo.ConsultationRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ConsultationScheduleViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

//    private val user: User? by lazy {
//        sharedPreference.getUserValue()
//    }

    private var parameter: HashMap<String?, Any?> = hashMapOf(
        "id_pasien" to sharedPreference.getUserValue()?.id
    )

    val scheduleList: MutableLiveData<List<Schedule>> by lazy {
        MutableLiveData<List<Schedule>>()
    }

    fun initSchedule() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getSchedule(
                parameter,
                onSuccess = {
                    setLoading(false)
                    scheduleList.postValue(it)
                },
                onError = {
                    setLoading(false)
                    scheduleList.postValue(listOf())
                }
            )
        }
    }

}