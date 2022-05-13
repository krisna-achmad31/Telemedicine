package com.telemedicine.indihealth.ui.bottomsheet.diagnosis

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ConsultationDoctorRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConsultationDiagnosisViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationDoctorRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    private val user: User? by lazy {
        sharedPreference.getUserValue()
    }
    var schedule: ScheduleDoctorConsultation? = null

    val diagnosis: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun postDiagnosis() {
        setLoading(true)
        val param = hashMapOf<String,String?>(
            "id_jadwal_konsultasi" to schedule?.id,
            "id_dokter" to schedule?.id_dokter,
            "id_pasien" to schedule?.id_pasien,
            "id_registrasi" to schedule?.id_registrasi,
            "diagnosis" to diagnosis.value
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postDiagnosis(
                param,
                onSuccess = {
                    setLoading(false)
                    setStatus(
                        hashMapOf(
                            "status" to "success",
                            "msg" to it
                        )
                    )
//                    diagnosis.postValue("")
                    sharedPreference.setBoolean(AppVar.IS_CALL_DIAGNOSIS_EXIST,true)
                },
                onError = {
                    setLoading(false)
                    setStatus(
                        hashMapOf(
                            "status" to "failed",
                            "msg" to it
                        )
                    )
//                    diagnosis.postValue("")

                }
            )
        }
    }

}