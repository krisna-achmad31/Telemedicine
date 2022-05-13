package com.telemedicine.indihealth.ui.activity.doctor

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.GlobalItem
import com.telemedicine.indihealth.model.Prescription
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ConsultationDoctorRepository
import com.telemedicine.indihealth.network.repo.ProfileRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ConsultationCallViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationDoctorRepository,
    private val sharedPreference: SharedPreferenceApp,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {
    val durasi: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    fun setProfileAsync(it: User?) {
        durasi.postValue(it)
    }

    init {
        viewModelScope.launch {
            var condition: HashMap<String?, String?> = hashMapOf("id_dokter" to sharedPreference.getUserValue()?.id)
            profileRepository.getDoctorProfile(condition,
                onSuccess = {
                    if (it!!.use_diagnosa == "1") {
                        showDiagnosis.postValue(true)
                    } else {
                        showDiagnosis.postValue(false)
                    }
                    setProfileAsync(it)
                },
                onError = {
                    showDiagnosis.postValue(false)
                }
            )
        }
    }

    val user: User? by lazy {
        sharedPreference.getUserValue()
    }

    val showDiagnosis = MutableLiveData<Boolean>(false)

    var scheduleDoctorConsultation: ScheduleDoctorConsultation? = null

    var roomNameFinal: String? = null

    var diagnosis: String? = null

    var prescription: ArrayList<Prescription>? = null

    var isPrescriptionSend : Boolean = false

    fun setRoomFinal() {
        roomNameFinal = getRoomName()
    }

    private val dbReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }


    private fun getRandom(): String {
        val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
        var result = ""
        for (i in 0 until 15) {
            result += source.random()
        }
        return result
    }

    private fun getRoomName(): String {
        val random = getRandom()
        return "telemedicine_lintas_${scheduleDoctorConsultation?.id}${scheduleDoctorConsultation?.id_pasien}$random"
    }

    fun postCallVideo() {
        setLoading(true)
        val param = hashMapOf(
            "id_pasien" to scheduleDoctorConsultation?.id_pasien,
            "id_dokter" to scheduleDoctorConsultation?.id_dokter,
            "id_jadwal_konsultasi" to scheduleDoctorConsultation?.id,
            "roomName" to roomNameFinal
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postCallVideo(
                param,
                onSuccess = {
                    setLoading(false)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                }
            )
        }
    }


    private fun postEndCall() {
        setLoading(true)
        val param = hashMapOf(
            "reg" to scheduleDoctorConsultation?.id,
            "id_pasien" to scheduleDoctorConsultation?.id_pasien,
            "id_dokter" to scheduleDoctorConsultation?.id_dokter,
            "id_jadwal_konsultasi" to scheduleDoctorConsultation?.id
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postEndCall(
                param,
                onSuccess = {
                    setLoading(false)
                    setStatus(
                        hashMapOf(
                            "type" to "endCall",
                            "status" to "success",
                            "msg" to it
                        )
                    )
                },
                onError = {
                    setLoading(false)
                    setStatus(
                        hashMapOf(
                            "type" to "endCall",
                            "status" to "failed",
                            "msg" to it
                        )
                    )
                    Timber.d("error = $it")
                }
            )
        }
    }

    val conditionBeforeEnded: MutableLiveData<Event<String>> by lazy {
        MutableLiveData<Event<String>>()
    }

    fun checkBeforeEnded() {
        Timber.d("isCallDiagnosis ${sharedPreference.getBoolean(AppVar.IS_CALL_DIAGNOSIS_EXIST)}")
        Timber.d("isCallRecipe ${sharedPreference.getBoolean(AppVar.IS_CALL_RECIPE_EXIST)}")
        if (!sharedPreference.getBoolean(AppVar.IS_CALL_DIAGNOSIS_EXIST)!!) {
            conditionBeforeEnded.postValue(Event(AppVar.IS_CALL_DIAGNOSIS_EXIST))
            Timber.d("isCallDiagnosis not exist")
        } else if (!sharedPreference.getBoolean(AppVar.IS_CALL_RECIPE_EXIST)!!) {
            conditionBeforeEnded.postValue(Event(AppVar.IS_CALL_RECIPE_EXIST))
            Timber.d("isCallRecipes not exist")
        } else {
            conditionBeforeEnded.postValue(Event("success"))
            Timber.d("checkEnded Success")
        }
    }

    fun onDialogPrescriptionClicked() {
        sharedPreference.setBoolean(AppVar.IS_CALL_RECIPE_EXIST, true)
        checkBeforeEnded()
    }

    fun onCallEnded() {
        sharedPreference.setBoolean(AppVar.IS_CALL_RECIPE_EXIST, false)
        sharedPreference.setBoolean(AppVar.IS_CALL_DIAGNOSIS_EXIST, false)
        postEndCall()
    }

    fun clearChat() {
        dbReference.child(
            "${AppVar.PATH_CHATS}/${
                scheduleDoctorConsultation
                    ?.id_dokter
            }_${scheduleDoctorConsultation?.id_pasien}"
        ).removeValue()
            .addOnCompleteListener {
                Timber.d("Clear chat success = $it")
            }
            .addOnFailureListener {
                Timber.e("Clear chat failed = $it")
            }
    }

    val rejectCallStatus: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    fun postEndCallByDokter() {
        setLoading(true)
        val param = hashMapOf(
            "id_pasien" to scheduleDoctorConsultation?.id_pasien,
            "id_dokter" to scheduleDoctorConsultation?.id_dokter
        )
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("postEndCallByDokter = ${scheduleDoctorConsultation?.id_pasien}")
            mRepository.postEndCallByDokter(
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
}