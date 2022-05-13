package com.telemedicine.indihealth.ui.fragment.doctor.records

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

class DoctorMedicalRecordsViewModel @ViewModelInject constructor(
    private val mRepository: MedicalRecordsRepository,
    private val sharedPreference: SharedPreferenceApp

) : BaseViewModel() {

    private val TAG="com.telemedicine.indihealth.ui.fragment.doctor.records.DoctorMedicalRecordsViewModel"

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

    val queueList: MutableLiveData<List<MedicalRecords>> by lazy {
        MutableLiveData<List<MedicalRecords>>()
    }

    var namePatientList: List<String> = mutableListOf()
    var idPatientList: List<String?> = mutableListOf()
    var name: String? = ""
    var positionName: Int = 0
    val nameList: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    private fun setQueueListAsync(it: List<MedicalRecords>?) {
        var stat: String? = "0"
        var ind: Int = 0
        for (value in it!!) {
            stat = "0"
            for (row in idPatientList) {
                if (row == value.id_pasien){
                    stat = "1"
                }
            }
            if (stat == "0") {
                namePatientList += value.getNamePatient
                idPatientList += value.id_pasien
                if (name == "") {
                    if (ind == 0) {
                        name = value.getNamePatient
                        ind += 1
                    }
                }
            }
        }
        var temp: List<MedicalRecords> = mutableListOf()
        for (value in it){
            if (value.getNamePatient == name) {
                temp += value
            }
        }
        queueList.postValue(temp)
        nameList.postValue(namePatientList)
    }

    fun getQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getMedicalRecords(
                id_dokter = sharedPreference.getUserValue()?.id,
                id_pasien = "all",
                onSuccess = {
                    setLoading(false)
                    Timber.d("$TAG Rekam Medik: $it")
                    //queueList.postValue(it)
                    setQueueListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("$TAG error = $it")
                    setQueueListAsync(listOf())
                }
            )
        }
    }

    fun initQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getMedicalRecords(
                id_dokter = sharedPreference.getUserValue()?.id,
                id_pasien = "all",
                onSuccess = {
                    setLoading(false)
                    Timber.d("$TAG Rekam Medik: $it")
                    setQueueListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("$TAG error = $it")
                    setQueueListAsync(listOf())
                }
            )
        }
    }
}