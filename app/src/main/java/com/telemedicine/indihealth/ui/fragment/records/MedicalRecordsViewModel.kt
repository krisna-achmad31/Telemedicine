package com.telemedicine.indihealth.ui.fragment.records

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.MedicalRecords
import com.telemedicine.indihealth.network.repo.MedicalRecordsRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MedicalRecordsViewModel @ViewModelInject constructor(
    private val mRepository: MedicalRecordsRepository,
    private val sharedPreference: SharedPreferenceApp

) : BaseViewModel() {

    val profileDoctorList: MutableLiveData<List<MedicalRecords>> by lazy {
        MutableLiveData<List<MedicalRecords>>()
    }

    val isProfileDoctorListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    fun setProfileDoctorListExist(boolean: Boolean?) {
        isProfileDoctorListExist.value = boolean
    }

    var polyDoctorList: List<String?> = mutableListOf()
    var idDcoctorList: List<String?> = mutableListOf()
    var poli: String? = ""
    var positionPoly: Int = 0
    val polyList: MutableLiveData<List<String?>> by lazy {
        MutableLiveData<List<String?>>()
    }

    private fun setProfileDoctorListAsync(it: List<MedicalRecords>?) {
        var stat: String? = "0"
        var ind: Int = 0
        for (value in it!!) {
            stat = "0"
            for (row in idDcoctorList) {
                if (row == value.id_dokter){
                    stat = "1"
                }
            }
            if (stat == "0") {
                polyDoctorList += value.poli
                idDcoctorList += value.id_dokter
//                if (poli == "") {
//                    if (ind == 0){
//                        poli = value.poli
//                        ind += 1
//                    }
//                }
            }
        }
        var temp: List<MedicalRecords> = mutableListOf()
        for (value in it){
            if(poli != ""){
                if (value.poli == poli) {
                    temp += value
                }
            }else {
                temp += value
            }
        }
        profileDoctorList.postValue(temp)
        polyList.postValue(polyDoctorList)
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }

    fun getConsultationDoctor() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getMedicalRecords(
                id_dokter = "all",
                id_pasien = sharedPreference.getUserValue()?.id,

                onSuccess = {
                    setLoading(false)
                    setProfileDoctorListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setProfileDoctorListAsync(listOf())
                }
            )
        }
    }

    fun initConsultationDoctor() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getMedicalRecords(
                id_dokter = "all",
                id_pasien = sharedPreference.getUserValue()?.id,

                onSuccess = {
                    setLoading(false)
                    setProfileDoctorListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setProfileDoctorListAsync(listOf())
                }
            )
        }
    }
}