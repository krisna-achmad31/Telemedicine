package com.telemedicine.indihealth.ui.activity.additional

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.AdditionalInfo
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.network.repo.ConsultationDoctorRepository
import com.telemedicine.indihealth.network.repo.MedicalRecordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Surya Mahadi on 11/13/2021
 */
class AdditionalVIewModel @ViewModelInject constructor(
    val repository: ConsultationDoctorRepository
) : BaseViewModel() {

    var planning: String = ""
    var isLaboratory: Boolean = false
    var isRadiology: Boolean = false
    var inspection: String = ""
    var schedule: ScheduleDoctorConsultation? = null
    var conslusion: String = ""
    val additionalInfo = MutableLiveData<AdditionalInfo>()

    fun postAdditionalInfo() = viewModelScope.launch {
        setLoading(true)
        val param = hashMapOf<String, String>(
            "id_jadwal_konsultasi" to schedule!!.id!!,
            "planning" to planning,
            "laboratorium" to if(isLaboratory) "1" else "0",
            "radiologi" to if(isRadiology) "1" else "0",
            "pemeriksaan" to inspection,
            "kesimpulan" to conslusion
        )
        repository.postAdditionalInfo(param,
            onSuccess = {
                setLoading(false)
                setStatus(
                    hashMapOf(
                        "status" to "success",
                        "msg" to it
                    )
                )
            },
            onError = {
                setLoading(false)
                setStatus(
                    hashMapOf(
                        "status" to "failed",
                        "msg" to it
                    )
                )
            }
        )
    }

    fun getAdditionalInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val param = hashMapOf(
                "id_jadwal_konsultasi" to schedule?.id
            )
            repository.getAdditionalInfo(
                param,
                onSuccess = {
                    Timber.d("Additional Info: $it")
                    val info = if (it == null) {
                        AdditionalInfo()
                    } else {
                        it
                    }
                    additionalInfo.postValue(info)
                    planning = info.planning ?: ""
                    isLaboratory = (info.laboratorium == "1")
                    isRadiology = (info.radiologi == "1")
                    conslusion = info.kesimpulan ?: ""
                    inspection = info.pemeriksaan ?: ""
                },
                {
                    Timber.e(it)
                }
            )
        }
    }

}