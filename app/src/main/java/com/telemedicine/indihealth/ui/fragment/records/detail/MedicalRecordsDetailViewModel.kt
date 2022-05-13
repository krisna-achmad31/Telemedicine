package com.telemedicine.indihealth.ui.fragment.records.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.Drug
import com.telemedicine.indihealth.model.MedicalRecords
import com.telemedicine.indihealth.model.MedicalRecordsDetail
import com.telemedicine.indihealth.network.repo.MedicalRecordsRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MedicalRecordsDetailViewModel @ViewModelInject constructor(
    private val mRepository: MedicalRecordsRepository,
    private val sharedPreference: SharedPreferenceApp

) : BaseViewModel() {

    val isQueueListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    val orderStatus : MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply { value = null }
    }

    fun setQueueListExist(boolean: Boolean?) {
        isQueueListExist.value = boolean
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }

    var medicalRecords: MedicalRecords? = null

    val medicalRecordsDetail: MutableLiveData<MedicalRecordsDetail> by lazy {
        MutableLiveData<MedicalRecordsDetail>()
    }

    val queueList: MutableLiveData<List<Drug>> by lazy {
        MutableLiveData<List<Drug>>()
    }

    private fun setQueueListAsync(it: MedicalRecordsDetail?) {
        orderStatus.postValue(it?.order_status)
        medicalRecordsDetail.postValue(it)
        queueList.postValue(it!!.list_obat)
    }

    fun getQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getMedicalRecordsDetail(
                id_jadwal_konsultasi = medicalRecords?.id_jadwal_konsultasi,
                id_pasien = medicalRecords?.id_pasien,
                onSuccess = {
                    setLoading(false)
                    setQueueListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setQueueListAsync(null)
                }
            )
        }
    }

    fun initQueue() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getMedicalRecordsDetail(
                id_jadwal_konsultasi = medicalRecords?.id_jadwal_konsultasi,
                id_pasien = medicalRecords?.id_pasien,
                onSuccess = {
                    setLoading(false)
                    setQueueListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setQueueListAsync(null)
                }
            )
        }
    }

    fun getRole():String{
        return sharedPreference.getUserValue()?.role!!
    }
}