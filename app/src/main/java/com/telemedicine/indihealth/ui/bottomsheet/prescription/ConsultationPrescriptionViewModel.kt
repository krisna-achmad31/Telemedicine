package com.telemedicine.indihealth.ui.bottomsheet.prescription

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.Medicine
import com.telemedicine.indihealth.model.Prescription
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ConsultationDoctorRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ConsultationPrescriptionViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationDoctorRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    private var prescriptionList: MutableList<Prescription> = mutableListOf()
    private val user: User? by lazy {
        sharedPreference.getUserValue()
    }

    val prescription : MutableLiveData<Event<Prescription>> by lazy {
        MutableLiveData<Event<Prescription>>()
    }
    var medicine: Medicine? = null
    var schedule: ScheduleDoctorConsultation? = null

    val total: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val rule: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun sendPrescription(it: MutableList<Prescription>){
        prescriptionList = it
//        clearPrescription()
        postPrescription()
    }

    fun clearPrescription(i: Int = 0) {
        setLoading(true)
        val param = hashMapOf(
            "id_registrasi" to schedule?.id_registrasi
        )

//        val param = schedule?.id_registrasi

        val data = prescriptionList[i]

//        val param = hashMapOf<String,String?>(
//            "id_jadwal_konsultasi" to schedule?.id,
//            "id_dokter" to schedule?.id_dokter,
//            "id_pasien" to schedule?.id_pasien,
//            "keterangan" to data.keterangan,
//            "jumlah_obat" to data.jumlahObat,
//            "id_obat" to data.obat?.id
//        )
        Timber.d("param = $param")
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.clearPrescription(
                param,
                onSuccess = {
                    setLoading(false)
                    setStatus(
                        hashMapOf(
                            "status" to "success",
                            "msg" to it
                        )
                    )
//                    if(i+1 < prescriptionList.size){
//                        clearPrescription(i-1)
//                    }else{
//                        setStatus(
//                            hashMapOf(
//                                "status" to "success",
//                                "msg" to it
//                            )
//                        )
//                        sharedPreference.setBoolean(AppVar.IS_CALL_RECIPE_EXIST,true)
//                    }
                },
                onError = {
                    setLoading(false)
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

    fun postPrescription(i: Int = 0) {
        setLoading(true)
        val data = prescriptionList[i]

        val param = hashMapOf<String,String?>(
            "id_jadwal_konsultasi" to schedule?.id,
            "id_dokter" to schedule?.id_dokter,
            "id_pasien" to schedule?.id_pasien,
            "keterangan" to data.keterangan,
            "jumlah_obat" to data.jumlahObat,
            "id_obat" to data.obat?.id
        )
        Timber.d("param = $param")
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postPrescription(
                param,
                onSuccess = {
                    setLoading(false)
                    if(i+1 < prescriptionList.size){
                        postPrescription(i+1)
                    }
                    else{
                        setStatus(
                            hashMapOf(
                                "status" to "success",
                                "msg" to it
                            )
                        )
                        sharedPreference.setBoolean(AppVar.IS_CALL_RECIPE_EXIST,true)
                    }
                },
                onError = {
                    setLoading(false)
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

    fun addMedicineToPrescription(){
        val prescriptionItem = Prescription(medicine,total.value,rule.value, medicine?.unit)
        prescription.value = Event(prescriptionItem)
        Timber.d("prescription = $prescription")
        clear()

    }

    fun clear(){
        medicine = null
        total.value = ""
        rule.value = ""
    }
}