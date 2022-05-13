package com.telemedicine.indihealth.ui.fragment.doctor.consultation.schedule

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.network.repo.ConsultationDoctorRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ConsultationScheduleDoctorViewModel @ViewModelInject constructor(
    private val repo: ConsultationDoctorRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    val scheduleConsultationDoctorList : MutableLiveData<List<ScheduleDoctorConsultation>> by lazy {
        MutableLiveData<List<ScheduleDoctorConsultation>>()
    }
    fun getScheduleDoctor() {
        setLoading(true)
        val parameter: HashMap<String?, Any?> = hashMapOf("id_dokter" to sharedPreference.getUserValue()?.id)
        viewModelScope.launch(Dispatchers.IO) {
            repo.getScheduleDoctor(
                parameter,
                onSuccess = {
                    setLoading(false)
                    scheduleConsultationDoctorList.postValue(it)
                    Timber.d("User = ${sharedPreference.getUserValue()}")
                },
                onError = {
                    setLoading(false)
                    scheduleConsultationDoctorList.postValue(listOf())
                }
            )
        }
    }
}