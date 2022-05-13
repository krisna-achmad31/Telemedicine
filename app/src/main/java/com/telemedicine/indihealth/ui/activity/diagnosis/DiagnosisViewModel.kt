package com.telemedicine.indihealth.ui.activity.diagnosis

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.model.Diagnosis
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.network.repo.ConsultationDoctorRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class DiagnosisViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationDoctorRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    var schedule: ScheduleDoctorConsultation? = null
    var diagnosis: String? = null

    val diagnosisList : MutableLiveData<List<Diagnosis>> by lazy {
        MutableLiveData<List<Diagnosis>>()
    }

    fun getDiagnosisList(keyWord: String = "") {
        val params = hashMapOf<String?, Any?>(
            "keyword" to keyWord,
            "limit" to "100"
        )
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getDiagnosis(
                params,
                onSuccess = {
                    setLoading(false)
                    diagnosisList.postValue(it)
                },
                onError = {
                    setLoading(false)
                    diagnosisList.postValue(listOf())
                    Timber.d("error = $it")
                }
            )
        }
    }

    fun postDiagnosis(id: String, nama: String?) {
        this.diagnosis = nama
        setLoading(true)
        val param = hashMapOf<String,String?>(
            "id_jadwal_konsultasi" to schedule?.id,
            "id_dokter" to schedule?.id_dokter,
            "id_pasien" to schedule?.id_pasien,
            "id_registrasi" to schedule?.id_registrasi,
            "diagnosis" to id
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