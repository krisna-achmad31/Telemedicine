package com.telemedicine.indihealth.ui.fragment.consultation.payment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.Payment
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ConsultationRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ConsultationPaymentViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    private val user: User? by lazy {
        sharedPreference.getUserValue()
    }

    val consultationPaymentList: MutableLiveData<List<Payment>> by lazy {
        MutableLiveData<List<Payment>>()
    }

    private fun setConsultationPaymentList(list: List<Payment>?){
        consultationPaymentList.postValue(list)
    }

    val isConsultationPaymentListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }
    fun setConsultationPaymentListExist(boolean: Boolean){
        isConsultationPaymentListExist.postValue(boolean)
    }
    var parameters: HashMap<String?, Any?> = hashMapOf()

    fun initConsultationPaymentList() {
        setLoading(true)
        val parameter: HashMap<String?, String?> = hashMapOf("id_pasien" to sharedPreference.getUserValue()?.id)
        Timber.d("user = ${sharedPreference.getUserValue()}")
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getPaymentList(
                parameter,
                onSuccess = {
                    setLoading(false)
                    setConsultationPaymentList(it)
                },
                onError = {
                    setLoading(false)
                    setConsultationPaymentList(listOf())
                }
            )
        }
    }

    fun updateCanceledOnProcessPayment() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateCanceledOnProcessPayment(
                parameters,
                onSuccess = {
                    initConsultationPaymentList()
                },
                onError = {
                    setLoading(false)
                }
            )
        }
    }

    fun deletePayment() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deletePayment(
                parameters,
                onSuccess = {
                    initConsultationPaymentList()
                },
                onError = {
                }
            )
        }
    }
}