package com.telemedicine.indihealth.ui.fragment.doctor.assesment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.DoctorAssesment
import com.telemedicine.indihealth.network.repo.DoctorAssesmentRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class DoctorAssesmentPatientViewModel @ViewModelInject constructor(
    private val mRepository: DoctorAssesmentRepository,
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

    val queueList: MutableLiveData<List<DoctorAssesment>> by lazy {
        MutableLiveData<List<DoctorAssesment>>()
    }

    var namePatientList: List<String> = mutableListOf()
    var idPatientList: List<String?> = mutableListOf()
    var name: String? = ""
    var positionName: Int = 0
    val nameList: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    private fun setQueueListAsync(it: List<DoctorAssesment>?) {
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
                namePatientList += value.getName
                idPatientList += value.id_pasien
                if (name == "") {
                    if (ind == 0) {
                        name = value.getName
                        ind += 1
                    }
                }
            }
        }
        var temp: List<DoctorAssesment> = mutableListOf()
        for (value in it){
            if (value.getName == name) {
                temp += value
            }
        }
        queueList.postValue(temp)
        nameList.postValue(namePatientList)
    }

    fun getQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getAssesmentPatient(
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
            mRepository.getAssesmentPatient(
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