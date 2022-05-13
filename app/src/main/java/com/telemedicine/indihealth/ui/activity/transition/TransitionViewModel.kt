package com.telemedicine.indihealth.ui.activity.transition

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.Assessment
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ConsultationDoctorRepository
import com.telemedicine.indihealth.network.repo.ConsultationRepository
import com.telemedicine.indihealth.network.repo.PharmacyRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class TransitionViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationDoctorRepository,
    private val sharedPreference: SharedPreferenceApp,
    private val pharmacyRepository: PharmacyRepository,
    private val consultationRepository: ConsultationRepository
) : BaseViewModel() {

    val user: User? by lazy {
        sharedPreference.getUserValue()
    }

    fun setRoomIsCalled(){
        sharedPreference.setBoolean(AppVar.IS_CALL_NOTIFICATION_EXIST,true)
    }

    fun setRoomIsDestroyed(){
        sharedPreference.setBoolean(AppVar.IS_CALL_NOTIFICATION_EXIST,false)
    }

    var roomName: String? = ""
    var idDokter: String? = ""
    var idKonsultasi: String? = ""
    var message: String? = ""
    var idFarmasi: String? = ""

    val rejectCallStatus: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    val answerCallStatus: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    val assessment: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    fun getUserValue() : User? {
        return sharedPreference.getUserValue()
    }

    fun postRejectCallDoctor() {
        setLoading(true)
        val param = hashMapOf(
            "id_pasien" to sharedPreference.getUserValue()?.id,
            "id_dokter" to idDokter
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postRejectCall(
                param,
                onSuccess = {
                    setLoading(false)
                    rejectCallStatus.postValue(Event(true))
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    rejectCallStatus.postValue(Event(false))
                }
            )
        }
    }

    fun postRejectCall() {
        if(idFarmasi != null) {
            postRejectCallPharmacy()
        } else {
            postRejectCallDoctor()
        }
    }

    fun postAnswerCallDoctor() {
        setLoading(true)
        val param = hashMapOf(
            "id_pasien" to sharedPreference.getUserValue()?.id,
            "id_dokter" to idDokter,
            "id_jadwal_konsultasi" to idKonsultasi
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postAnswerCall(
                param,
                onSuccess = {
                    setLoading(false)
                    answerCallStatus.postValue(Event(true))
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    answerCallStatus.postValue(Event(false))
                }
            )
        }
    }

    fun postAnswerCall() {
        if(idFarmasi != null) {
            postAnswerCallPharmacy()
        } else {
            postAnswerCallDoctor()
        }
    }

    fun postEndCallByPasien(string: String?) {
        setLoading(true)
        val param = hashMapOf(
            "id_pasien" to sharedPreference.getUserValue()?.id,
            "id_dokter" to string
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postEndCallByPasien(
                param,
                onSuccess = {
                    setLoading(false)
                    rejectCallStatus.postValue(Event(true))
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    rejectCallStatus.postValue(Event(false))
                }
            )
        }
    }

    fun postAnswerCallPharmacy() {
        setLoading(true)
        val param = hashMapOf(
            "id_farmasi" to idFarmasi,
            "id_user" to sharedPreference.getUserValue()?.id
        )
        viewModelScope.launch(Dispatchers.IO) {
            pharmacyRepository.postAnswerCall(
                param,
                onSuccess = {
                    setLoading(false)
                    answerCallStatus.postValue(Event(true))
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    answerCallStatus.postValue(Event(false))
                }
            )
        }
    }

    fun postRejectCallPharmacy() {
        setLoading(true)
        val param = hashMapOf(
            "id_farmasi" to idFarmasi,
            "id_user" to sharedPreference.getUserValue()?.id
        )
        viewModelScope.launch(Dispatchers.IO) {
            pharmacyRepository.postRejectCall(
                param,
                onSuccess = {
                    setLoading(false)
                    rejectCallStatus.postValue(Event(true))
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    rejectCallStatus.postValue(Event(false))
                }
            )
        }
    }

    fun getConsultation() {
        viewModelScope.launch(Dispatchers.IO) {
            val param: HashMap<String?, Any?> = hashMapOf(
                "id_pasien" to sharedPreference.getUserValue()?.id,
                "id_jadwal_konsultasi" to idKonsultasi
            )
            consultationRepository.getAssessment(
                param,
                onSuccess = {
                    assessment.postValue(Event(true))
                },
                onError = {
                    assessment.postValue(Event(false))
                    Timber.d("error = $it")
                }
            )
        }
    }
}