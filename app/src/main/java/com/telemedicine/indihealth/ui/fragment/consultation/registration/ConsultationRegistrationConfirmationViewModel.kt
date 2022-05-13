package com.telemedicine.indihealth.ui.fragment.consultation.registration

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ConsultationRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ConsultationRegistrationConfirmationViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationRepository,
    private val sharedPreference: SharedPreferenceApp
): BaseViewModel(){

    val profile: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    val showTOC: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }
    private var condition = HashMap<String, Any>()
    var idJadwal =0
    var idDokter =0

    fun getDoctorProfile() {
        setLoading(true)
        condition = hashMapOf("id_dokter" to idDokter)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getProfileDoctor(
                condition,
                onSuccess = {
                    setLoading(false)
                    profile.postValue(it)
                    Timber.d("ConsultationRegistrationConfirmationViewModel $it")
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                }
            )
        }
    }

    fun showTOC() {
        showTOC.postValue(Event(true))
    }

    fun postRegistrationDoctor() {
        Timber.d("isCalled")
        val hashMap = hashMapOf<String, Any?>(
            "id_jadwal" to idJadwal,
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