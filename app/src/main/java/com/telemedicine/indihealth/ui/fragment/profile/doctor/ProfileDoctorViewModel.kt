package com.telemedicine.indihealth.ui.fragment.profile.doctor

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.ProfileDoctor
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ProfileRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ProfileDoctorViewModel @ViewModelInject constructor(
    private val mRepository: ProfileRepository,
    private val sharedPreference: SharedPreferenceApp

) : BaseViewModel() {

    var condition: HashMap<String, Any> = hashMapOf()

    val profileDoctorList: MutableLiveData<List<ProfileDoctor>> by lazy {
        MutableLiveData<List<ProfileDoctor>>()
    }

    val isProfileDoctorListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    fun setProfileDoctorListExist(boolean: Boolean?) {
        isProfileDoctorListExist.value = boolean
    }

    private fun setProfileDoctorListAsync(it: List<ProfileDoctor>?) {
        profileDoctorList.postValue(it)
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }

    private val user: User? by lazy {
        sharedPreference.getUserValue()
    }


    fun getConsultationDoctor() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getProfileDoctor(
                condition,
                onSuccess = {
                    setLoading(false)
                    setProfileDoctorListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setProfileDoctorListAsync(listOf())
                }
            )
        }
    }

    fun initConsultationDoctor() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getProfileDoctor(
                hashMapOf(),
                onSuccess = {
                    setLoading(false)
                    setProfileDoctorListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setProfileDoctorListAsync(listOf())
                }
            )
        }
    }
}