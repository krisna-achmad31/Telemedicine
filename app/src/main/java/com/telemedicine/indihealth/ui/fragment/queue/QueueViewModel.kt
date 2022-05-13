package com.telemedicine.indihealth.ui.fragment.queue

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.Queue
import com.telemedicine.indihealth.network.repo.QueueRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class QueueViewModel @ViewModelInject constructor(
    private val mRepository: QueueRepository,
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

    val queueList: MutableLiveData<List<Queue>> by lazy {
        MutableLiveData<List<Queue>>()
    }

    var polyDoctorList: List<String?> = mutableListOf()
    var idDcoctorList: List<String?> = mutableListOf()
    var poli: String? = ""
    var positionPoly: Int = -1
    var masterQueueList: List<Queue>? = mutableListOf()

    val poliText: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = "Pilih"
        }
    }

    private fun setQueueListAsync() {
        var stat: String? = "0"
        var ind: Int = 0
        for (value in masterQueueList!!) {
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
        var temp: List<Queue> = mutableListOf()
        for (value in masterQueueList!!){
            if(poli != "") {
                if (value.poli == poli) {
                    temp += value
                }
            }else {
                temp += value
            }
        }
        polyDoctorList = polyDoctorList.distinct()
        queueList.postValue(temp)
    }

    fun getQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getQueue(
                id_pasien = sharedPreference.getUserValue()?.id,
                status = "0",
                onSuccess = {
                    setLoading(false)
                    masterQueueList = it
                    setQueueListAsync()
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    masterQueueList = listOf()
                    setQueueListAsync()
                }
            )
        }
    }

    fun initQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getQueue(
                id_pasien = sharedPreference.getUserValue()?.id,
                status = "0",
                onSuccess = {
                    setLoading(false)
                    masterQueueList = it
                    setQueueListAsync()
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    masterQueueList = listOf()
                    setQueueListAsync()
                }
            )
        }
    }

    fun setSelectedPoli(position: Int) {
        this.positionPoly = position
        if(position != -1){
            this.poli = polyDoctorList[position]
            poliText.postValue(poli)
        }else {
            poliText.postValue("Pilih")
            this.poli = ""
        }
        setQueueListAsync()
    }
}