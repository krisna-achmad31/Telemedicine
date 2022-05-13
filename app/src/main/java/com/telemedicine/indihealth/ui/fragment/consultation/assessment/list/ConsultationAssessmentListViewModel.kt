package com.telemedicine.indihealth.ui.fragment.consultation.assessment.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.Assessment
import com.telemedicine.indihealth.network.repo.ConsultationRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConsultationAssessmentListViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    val assessmentList: MutableLiveData<List<Assessment>> by lazy {
        MutableLiveData<List<Assessment>>()
    }

    val isAssessmentListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    private fun setAssessmentListAsync(list: List<Assessment>) {
        assessmentList.postValue(list)
    }

    fun setIsAssessmentLisExist(boolean: Boolean) {
        isAssessmentListExist.postValue(boolean)
    }

    fun fetchAssessmentList() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getAssessmentList(sharedPreference.getUserValue()?.id!!,
                onSuccess = {
                    setLoading(false)
                    setAssessmentListAsync(it?.data!!)
                },
                onError = {
                    setLoading(false)
                }
            )
        }
    }

    fun setAssessment(assessment: Assessment){
        sharedPreference.setAssessment(assessment)
    }

}