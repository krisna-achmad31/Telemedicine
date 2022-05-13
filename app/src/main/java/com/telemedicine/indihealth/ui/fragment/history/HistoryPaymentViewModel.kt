package com.telemedicine.indihealth.ui.fragment.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.HistoryConsultation
import com.telemedicine.indihealth.model.HistoryDrug
import com.telemedicine.indihealth.network.repo.HistoryPaymentRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class HistoryPaymentViewModel @ViewModelInject constructor(
    private val mRepository: HistoryPaymentRepository,
    private val sharedPreference: SharedPreferenceApp

) : BaseViewModel() {

    val historyConsultationList: MutableLiveData<List<HistoryConsultation>> by lazy {
        MutableLiveData<List<HistoryConsultation>>()
    }

    val historyDrugList: MutableLiveData<List<HistoryDrug>> by lazy {
        MutableLiveData<List<HistoryDrug>>()
    }

    val isHistoryConsultationListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    val isHistoryDrugListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    private var paymentFilter = ""
    private var type = 1

    private var historyConsultationMaster : List<HistoryConsultation>? = mutableListOf()
    private var historyDrugMaster : List<HistoryDrug>? = mutableListOf()

    fun setHistoryConsultationListExist(boolean: Boolean?) {
        isHistoryConsultationListExist.value = boolean
    }

    fun setHistoryDrugListExist(boolean: Boolean?) {
        isHistoryDrugListExist.value = boolean
    }

    private fun setHistoryConsultationListAsync(it: List<HistoryConsultation>?) {
        val list: ArrayList<HistoryConsultation> = ArrayList()
        for (i in it!!){
            if(paymentFilter.isNotEmpty()){
                if(i.metode_pembayaran == paymentFilter){
                    list.add(i)
                }
            } else {
                list.add(i)
            }
        }
        if(type == 1) {
            isHistoryConsultationListExist.postValue(list.isNotEmpty())
        }
        else {
            isHistoryConsultationListExist.postValue(true)
        }
        historyConsultationList.postValue(list)
    }

    private fun setHistoryDrugListAsync(it: List<HistoryDrug>?) {
        var tempString: String = ""
        var list: List<HistoryDrug> = mutableListOf()
        for (i in it!!){
            tempString = ""
            for (j in i.detail_obat){
                if (tempString != ""){
                    tempString += "\n"
                }
                tempString += "- $j"
            }
            i.list_obat = tempString
            list += i
        }
        val list2: ArrayList<HistoryDrug> = ArrayList()

        for (i in list){
            if(paymentFilter.isNotEmpty()){
                if(i.getPayment == paymentFilter){
                    list2.add(i)
                }
            }else {
                list2.add(i)
            }
        }
        if(type == 2){
            isHistoryDrugListExist.postValue(list2.isNotEmpty())
        }else{
            isHistoryDrugListExist.postValue(true)
        }
        historyDrugList.postValue(list2)
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }

    fun getHistoryConsultation() {
        setLoading(true)
        type = 1
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getHistoryPayment(
                id_pasien = sharedPreference.getUserValue()?.id,
                onSuccess = {
                    setLoading(false)
                    historyConsultationMaster=it
                    setHistoryConsultationListAsync(it)
                }
            ) {
                setLoading(false)
                Timber.d("error = $it")
                setHistoryConsultationListAsync(listOf())
            }
        }
        setLoading(false)
    }

    fun getHistoryDrug() {
        setLoading(true)
        type = 2
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getHistoryPaymentDrug(
                id_pasien = sharedPreference.getUserValue()?.id,
                onSuccess = {
                    setLoading(false)
                    historyDrugMaster=it
                    setHistoryDrugListAsync(it)
                }
            ) {
                setLoading(false)
                Timber.d("error = $it")
                setHistoryDrugListAsync(listOf())
            }
        }
        setLoading(false)
    }

    fun initHistoryConsultation() {
        setLoading(true)
        type = 1
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getHistoryPayment(
                id_pasien = sharedPreference.getUserValue()?.id,
                onSuccess = {
                    historyConsultationMaster=it
                    setLoading(false)
                    setHistoryConsultationListAsync(it)
                }
            ) {
                setLoading(false)
                Timber.d("error = $it")
                setHistoryConsultationListAsync(listOf())
            }
        }
        setLoading(false)
    }

    fun setPaymentFilter(payment: String) {
        this.paymentFilter = payment
        setHistoryConsultationListAsync(historyConsultationMaster)
        setHistoryDrugListAsync(historyDrugMaster)
    }
}