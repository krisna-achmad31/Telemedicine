package com.telemedicine.indihealth.ui.fragment.doctor.profile.show

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ProfileRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class DoctorProfileShowViewModel @ViewModelInject constructor(
    private val mRepository: ProfileRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    val profile: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    fun setProfileAsync(it: User?) {
        profile.postValue(it)
    }

    fun fetchProfile() {
        setLoading(true)
        var condition: HashMap<String?, String?> = hashMapOf("id_dokter" to sharedPreference.getUserValue()?.id)
        Timber.d("id dokter = ${sharedPreference.getUserValue()?.id}")
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getDoctorProfile(
                condition,
                onSuccess = {
                    setLoading(false)
                    setProfileAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setProfileAsync(null)
                }
            )
        }
    }
}