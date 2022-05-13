package com.telemedicine.indihealth.ui.fragment.schedule

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.ConsultationDoctor
import com.telemedicine.indihealth.model.FilterRegistrationDoctor
import com.telemedicine.indihealth.network.repo.ConsultationRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ScheduleDoctorViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationRepository,
    private val sharedPreference: SharedPreferenceApp

) : BaseViewModel() {

    var condition: HashMap<String, Any> = hashMapOf()

    val consultationDoctorList: MutableLiveData<List<ConsultationDoctor>> by lazy {
        MutableLiveData<List<ConsultationDoctor>>()
    }

    val isConsultationDoctorListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    fun setConsultationDoctorListExist(boolean: Boolean?) {
        isConsultationDoctorListExist.value = boolean
    }

    private fun setConsultationDoctorListAsync(it: List<ConsultationDoctor>?) {
        consultationDoctorList.postValue(it)
    }

    val polyList: MutableLiveData<List<FilterRegistrationDoctor>> by lazy {
        MutableLiveData<List<FilterRegistrationDoctor>>()
    }

    val isPolyListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    fun setPolyList(list: List<ConsultationDoctor>?) {
        val items = mutableListOf<FilterRegistrationDoctor>()
        list?.forEach {
            if (it.poli != "UGD")
                items
                    .add(
                        FilterRegistrationDoctor(
                            it.poli.toLowerCase(Locale.ROOT).capitalizeWords()
                        )
                    )
            else
                items.add(FilterRegistrationDoctor(it.poli))
        }
        isPolyListExist.postValue(items.size != 0)
        polyList.postValue(items.distinct())
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }

    fun getConsultationDoctor() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getConsultationDoctor(
                condition,
                onSuccess = {
                    setLoading(false)
                    setConsultationDoctorListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setConsultationDoctorListAsync(listOf())
                }
            )
        }
    }

    fun initConsultationDoctor() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getConsultationDoctor(
                hashMapOf(),
                onSuccess = {
                    setLoading(false)
                    setConsultationDoctorListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setConsultationDoctorListAsync(listOf())
                }
            )
        }
    }

    fun postRegistrationDoctor(consultationDoctor: ConsultationDoctor) {
        Timber.d("isCalled")
        val hashMap = hashMapOf<String, Any?>(
            "id_jadwal" to consultationDoctor.id,
            "id_pasien" to sharedPreference.getUserValue()?.id,
            "keterangan" to "Belum Bayar",
            "id_status_pembayaran" to 0,
            "id_fasyankes" to sharedPreference.getUserValue()?.id_fasyankes
        )
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postRegistrationDoctor(
                hashMap,
                onSuccess = {
                    setLoading(false)
                    setStatus(
                        hashMapOf(
                            "status" to "success",
                            "msg" to it
                        )
                    )
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setStatus(
                        hashMapOf(
                            "status" to "failed",
                            "msg" to it
                        )
                    )
                }
            )
        }
    }
}