package com.telemedicine.indihealth.ui.fragment.payment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.DoctorHistoryLog
import com.telemedicine.indihealth.model.HistoryConsultation
import com.telemedicine.indihealth.model.ListPaymetnMethod
import com.telemedicine.indihealth.network.repo.ConsultationRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class PaymentConfirmationViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    val queueList: MutableLiveData<List<ListPaymetnMethod>> by lazy {
        MutableLiveData<List<ListPaymetnMethod>>()
    }

    var owlexaApproved: Boolean = false

    private var virtualAccountListMaster : List<ListPaymetnMethod>? = mutableListOf()

    fun getTocOwlexa(){
        if(owlexaApproved) return
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO){
            mRepository
                .getTocOwlexa(
                    onSuccess = {
                        setLoading(false)
                        setStatus(
                            hashMapOf(
                                "status" to "SUCCESSTOC",
                                "msg" to it
                            )
                        )
                    },
                    onError = {
                        setLoading(false)
                        setStatus(
                            hashMapOf(
                                "title" to "Gagal Terhubung ke Server",
                                "status" to "failed",
                                "msg" to it
                            )
                        )
                    }
                )
        }
    }

    fun getVaList(){
        val list: ArrayList<ListPaymetnMethod> = ArrayList()
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO){
            mRepository.getVaList(
                onSuccess = {
//                    for (i in it!!){
//                        list.add(i)
//                    }
                    Timber.d("List VA nya : ${it.toString()}")
                    queueList.postValue(it)
                }
            ){
                setLoading(false)
                Timber.d("error = $it")
            }
        }
        setLoading(false)
    }

    fun getBankTransferList() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getBankTransferList(
                onSuccess = {
                    queueList.postValue(it)
                    Timber.d("List Bank Transfer nya : ${it.toString()}")
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                }
            )
        }
    }
}