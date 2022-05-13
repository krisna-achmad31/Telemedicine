package com.telemedicine.indihealth.ui.activity.prescription

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.Medicine
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ConsultationDoctorRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MedicineViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationDoctorRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    private val user: User? by lazy {
        sharedPreference.getUserValue()
    }

    val medicineList : MutableLiveData<List<Medicine>> by lazy {
        MutableLiveData<List<Medicine>>()
    }
    fun fetchMedicineList() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getMedicine(
                hashMapOf(),
                onSuccess = {
                    setLoading(false)
                    medicineList.postValue(it)
                },
                onError = {
                    setLoading(false)
                    medicineList.postValue(listOf())
                    Timber.d("error = $it")
                }
            )
        }
    }

}