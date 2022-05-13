package com.telemedicine.indihealth.ui.fragment.consultation.registration

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

class ConsultationRegistrationViewModel @ViewModelInject constructor(
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
        if (it?.size!!>0){
            Timber.d("List Konsultasi = ${it.get(0)}")
            val items = mutableListOf<ConsultationDoctor>()
            it.forEach {
                when (it.hari.capitalizeWords()) {
                    "Senin" -> {
                        it.intHari = 1
                    }
                    "Selasa" -> {
                        it.intHari = 2
                    }
                    "Rabu" -> {
                        it.intHari = 3
                    }
                    "Kamis" -> {
                        it.intHari = 4
                    }
                    "Jum'at" -> {
                        it.intHari = 5
                    }
                    "Sabtu" -> {
                        it.intHari = 6
                    }
                    else -> {
                        it.intHari = 7
                    }
                }
                items.add(it)
            }
            items.sortBy { it.intHari }
            consultationDoctorList.postValue(items)
        }else{
            consultationDoctorList.postValue(it)
        }
    }

    val polyList: ArrayList<String> = arrayListOf()
    var polySelectedPosition: Int = -1

    val isPolyListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    val polyText: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = "Pilih Poli"
        }
    }

    fun setPolyList(list: List<ConsultationDoctor>?) {
        Timber.d("poli Konsultasi = ${list?.get(0)}")
        val items = mutableListOf<FilterRegistrationDoctor>()
        for (row in list!!){
            if (row.poli != "UGD") {
                Timber.d("Foreach $items")
                items.add(
                    FilterRegistrationDoctor(
                        row.poli.toLowerCase(Locale.ROOT).capitalizeWords()
                    )
                )
            } else {
                items.add(FilterRegistrationDoctor(row.poli))
            }
        }
        /*list?.forEach {
            Timber.d("foreach")
            if (it.poli != "UGD") {
                Timber.d("Foreach $items")
                items.add(
                    FilterRegistrationDoctor(
                        it.poli.toLowerCase(Locale.ROOT).capitalizeWords()
                    )
                )
            } else {
                items.add(FilterRegistrationDoctor(it.poli))
            }
        }*/
        isPolyListExist.postValue(items.size != 0)
        Timber.d("isi poli Konsultasi = ${items.size}")
        polyList.clear()
        for (row in items.distinct()) {
            polyList.add(row.name)
        }
        Timber.d("isi poli Konsultasi = $items")
    }

    val dayList: ArrayList<String> = arrayListOf()
    var daySelectedPosition: Int = -1

    val isDayListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    val dayText: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = "Pilih Hari"
        }
    }

    fun setDayList(list: List<String>?) {
        val items = mutableListOf<FilterRegistrationDoctor>()
        list?.forEach {
            items.add(FilterRegistrationDoctor(it.capitalizeWords()))
        }
        isDayListExist.postValue(items.size != 0)
        dayList.clear()
        for (row in items.distinct()) {
            dayList.add(row.name)
        }
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }

//    private val user: User? by lazy {
//        sharedPreference.getUserValue()
//    }


    fun getConsultationDoctor() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getConsultationDoctor(
                condition,
                onSuccess = {
                    setLoading(false)
                    setConsultationDoctorListAsync(it)
                    Timber.d("List Sukses $it")
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
        condition= hashMapOf()
        Timber.d("Masuk Kondultasi")
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getConsultationDoctor(
                hashMapOf(),
                onSuccess = {
                    setLoading(false)
                    Timber.d("success konsultasi = $it")
                    setConsultationDoctorListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error konsultasi= $it")
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

    fun setSelectedDay(position: Int) {
        this.daySelectedPosition = position
        if(position != -1){
            dayText.postValue(dayList[position])
            this.condition["hari"] = dayList[position]
        }else {
            dayText.postValue("Pilih Hari")
            this.condition["hari"] = ""
        }
    }

    fun setSelectedPoly(position: Int) {
        this.polySelectedPosition = position
        if(position != -1){
            polyText.postValue(polyList[position])
            this.condition["poli"] = polyList[position]
        }else {
            polyText.postValue("Pilih Poli")
            this.condition["poli"] = ""
        }
    }
}