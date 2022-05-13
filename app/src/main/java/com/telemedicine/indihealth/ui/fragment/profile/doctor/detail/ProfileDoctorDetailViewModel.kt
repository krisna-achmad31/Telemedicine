package com.telemedicine.indihealth.ui.fragment.profile.doctor.detail

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

class ProfileDoctorDetailViewModel @ViewModelInject constructor(
    private val mRepository: ProfileRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    var profileDoctor: ProfileDoctor? = null

    val profile: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    val address: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun setProfileAsync(it: User?) {
        profile.postValue(it)
        if (it?.alamat_jalan != "" && it?.alamat_kota != "" && it?.alamat_provinsi != "") {
            address.postValue(it?.alamat_jalan + ", " + it?.alamat_kota + ", " + it?.alamat_provinsi)
        } else {
            address.postValue("Kosong")
        }
    }

    fun getProfile() {
        setLoading(true)
        var condition: HashMap<String?, String?> = hashMapOf("id" to sharedPreference.getUserValue()?.id)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getPatientProfile(
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