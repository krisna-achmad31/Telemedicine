package com.telemedicine.indihealth.ui.fragment.consultation.assessment.detail

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.AppVar.BASE_ADDITION_FILE_URL
import com.telemedicine.indihealth.model.Assessment
import com.telemedicine.indihealth.model.Schedule
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ConsultationRepository
import com.telemedicine.indihealth.network.repo.ProfileRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File


class ConsultationAssessmentViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationRepository,
    private val sharedPreference: SharedPreferenceApp,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {



    var onOpenImage: Boolean = false
    private val TAG =
        "com.telemedicine.indihealth.ui.fragment.consultation.assessment.detail.ConsultationAssessmentViewModel"

//    private val user: User? by lazy {
//        sharedPreference.getUserValue()
//    }


    val bloodPressure1: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val bloodPressure2: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private var parameter: HashMap<String?, Any?> = hashMapOf(
        "id_pasien" to sharedPreference.getUserValue()?.id
    )

    val assessment: MutableLiveData<Assessment?> by lazy {
        MutableLiveData<Assessment?>()
    }

    private var isAssessmentExist: Boolean? = false

    val scheduleList: MutableLiveData<List<Schedule>> by lazy {
        MutableLiveData<List<Schedule>>()
    }

    val photo: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val images = mutableListOf<Uri>()
    private var hasSelectImage = false

    fun initAssessment(idJadwalKonsultasi: String) {
        if (onOpenImage) return
        Timber.d("$TAG id_jadwal_konsultasi: $idJadwalKonsultasi")
        setLoading(true)
        val parameter: HashMap<String?, Any?> = hashMapOf(
            "id_pasien" to sharedPreference.getUserValue()?.id,
            "id_jadwal_konsultasi" to idJadwalKonsultasi
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getAssessment(
                parameter,
                onSuccess = {
                    images.clear()
                    it?.file_pemeriksaan_luar?.forEach { file ->
                        images.add("${BASE_ADDITION_FILE_URL}${file.file}".toUri())
                        Timber.d("${BASE_ADDITION_FILE_URL}${file.file}")
                    }

                    assessment.postValue(it)
                    parseBloodPressure(it!!.tekanan_darah)
                    Timber.d("$TAG success: ${assessment.value}")
                    isAssessmentExist = true
                    setLoading(false)
                },
                onError = {
                    val assessment2 = Assessment()
                    assessment2.id_jadwal_konsultasi = idJadwalKonsultasi
                    assessment.postValue(assessment2)
                    bloodPressure1.postValue("")
                    bloodPressure2.postValue("")
                    isAssessmentExist = false
                    Timber.d("$TAG failed: ${assessment.value}")
                    setLoading(false)
                }
            )
        }
    }

    private fun parseBloodPressure(tekananDarah: String?) {
        if (!tekananDarah.isNullOrEmpty()) {
            val splitter = tekananDarah.split("/")
            bloodPressure1.postValue(splitter[0])
            bloodPressure2.postValue(splitter[1])
            Timber.d("splitter ${splitter[0]}")
            Timber.d("splitter ${splitter[1]}")
        } else {
            bloodPressure1.postValue("")
            bloodPressure2.postValue("")
        }
    }

    fun initSchedule() {
        if (onOpenImage) return
        setLoading(true)
        val parameter: HashMap<String?, Any?> = hashMapOf(
            "id_pasien" to sharedPreference.getUserValue()?.id
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getSchedule(
                parameter,
                onSuccess = {
                    setLoading(false)
                    scheduleList.postValue(it)
                },
                onError = {
                    setLoading(false)
                    scheduleList.postValue(listOf())
                }
            )
        }
    }

    fun initScheduleDoctor(idPasien: String?) {
        val param: HashMap<String?, Any?> = hashMapOf(
            "id_pasien" to idPasien
        )
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getSchedule(
                param,
                onSuccess = {
                    Timber.d("$TAG schedule: $it")
                    setLoading(false)
                    scheduleList.postValue(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("$TAG schedule: $it")
                    scheduleList.postValue(listOf())
                }
            )
        }
    }

    fun initAssessment(idPasien: String?, id: String?) {
        if (onOpenImage) return
        val param: HashMap<String?, Any?> = hashMapOf(
            "id_pasien" to idPasien,
            "id_jadwal_konsultasi" to id
        )
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getAssessment(
                param,
                onSuccess = {
                    setLoading(false)
                    images.clear()
                    it?.file_pemeriksaan_luar?.forEach { file ->
                        images.add("${BASE_ADDITION_FILE_URL}${file.file}".toUri())
                        Timber.d("${BASE_ADDITION_FILE_URL}${file.file}")
                    }
                    assessment.postValue(it)
                    parseBloodPressure(it!!.tekanan_darah)
                    Timber.d("$TAG success: ${assessment.value}")
                    isAssessmentExist = true
                },
                onError = {
                    setLoading(false)
                    assessment.postValue(Assessment())
                    Timber.d("$TAG failed: ${assessment.value}")
                    isAssessmentExist = false
                }
            )
        }
    }

    private fun postAssessment() {
        setLoading(true)
        Timber.d("$TAG post: $parameter")
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postAssessment(
                parameter(),
                getMultipartImage(),
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
                    setStatus(
                        hashMapOf(
                            "status" to "success",
                            "msg" to it
                        )
                    )
                }
            )
        }
    }

    private fun updateAssessment() {
        Timber.d("$TAG post: $parameter")
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateAssessment(
                parameter(),
                getMultipartImage(),
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

    private fun parameter(): HashMap<String?, RequestBody?> {
        val bp1 = if(bloodPressure1.value.isNullOrEmpty()) {
            "0"
        } else {
            bloodPressure1.value
        }
        val bp2 = if(bloodPressure2.value.isNullOrEmpty()) {
            "0"
        } else {
            bloodPressure2.value
        }
        val bloodPressure = "$bp1/$bp2"
        return hashMapOf(
            "id_jadwal_konsultasi" to assessment.value?.id_jadwal_konsultasi?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "berat_badan" to assessment.value?.berat_badan?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "tinggi_badan" to assessment.value?.tinggi_badan?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "tekanan_darah" to bloodPressure.toRequestBody("text/plain".toMediaTypeOrNull()),
            "suhu" to assessment.value?.suhu?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "merokok" to radioButtonValue(assessment.value?.merokok).toRequestBody("text/plain".toMediaTypeOrNull()),
            "alkohol" to radioButtonValue(assessment.value?.alkohol).toRequestBody("text/plain".toMediaTypeOrNull()),
            "kecelakaan" to radioButtonValue(assessment.value?.kecelakaan).toRequestBody("text/plain".toMediaTypeOrNull()),
            "dirawat" to radioButtonValue(assessment.value?.dirawat).toRequestBody("text/plain".toMediaTypeOrNull()),
            "operasi" to radioButtonValue(assessment.value?.operasi).toRequestBody("text/plain".toMediaTypeOrNull()),
            "riwayat_penyakit" to assessment.value?.riwayat_penyakit?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "riwayat_alergi" to assessment.value?.riwayat_alergi?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "keluhan" to assessment.value?.keluhan?.toRequestBody("text/plain".toMediaTypeOrNull()),
//            "foto" to assessment.value?.foto?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "id_pasien" to sharedPreference.getUserValue()?.id?.toRequestBody("text/plain".toMediaTypeOrNull())
        )
    }

    private fun radioButtonValue(value: String?): String {
        if (value.isNullOrEmpty()) return "0"
        return when (value?.toInt()) {
            R.id.consultation_assessment_radio_1 -> "1"
            R.id.consultation_assessment_radio_2 -> "1"
            R.id.consultation_assessment_radio_3 -> "1"
            R.id.consultation_assessment_radio_4 -> "1"
            R.id.consultation_assessment_radio_5 -> "1"
            else -> "0"
        }
    }

    fun onAssessmentClicked() {
        if (isAssessmentExist!!) {
            Timber.d("Assessment is not empty, assessment.value = ${assessment.value} and Assessment() = ${Assessment()}")
            updateAssessment()
        } else {
            Timber.d("Assessment is empty, postAssessment = ${assessment.value} and Assessment() = ${Assessment()}")
            postAssessment()
        }
    }

    fun onAssessmentClicked2(idPasien: String?, id: String?) {
        if (isAssessmentExist!!) {
            Timber.d("Assessment is not empty, assessment.value = ${assessment.value} and Assessment() = ${Assessment()}")
            updateAssessment2(idPasien, id)
        } else {
            Timber.d("Assessment is empty, postAssessment = ${assessment.value} and Assessment() = ${Assessment()}")
            postAssessment2(idPasien, id)
        }
    }

    private fun postAssessment2(idPasien: String?, id: String?) {
        setLoading(true)
        val bp1 = if(bloodPressure1.value.isNullOrEmpty()) {
            "0"
        } else {
            bloodPressure1.value
        }
        val bp2 = if(bloodPressure2.value.isNullOrEmpty()) {
            "0"
        } else {
            bloodPressure2.value
        }
        val bloodPressure = "$bp1/$bp2"
        val param2 = hashMapOf<String?, RequestBody?>(
            "berat_badan" to assessment.value?.berat_badan?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "tinggi_badan" to assessment.value?.tinggi_badan?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "tekanan_darah" to bloodPressure?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "suhu" to assessment.value?.suhu?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "merokok" to radioButtonValue(assessment.value?.merokok)?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "alkohol" to radioButtonValue(assessment.value?.alkohol)?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "kecelakaan" to radioButtonValue(assessment.value?.kecelakaan)?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "dirawat" to radioButtonValue(assessment.value?.dirawat)?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "operasi" to radioButtonValue(assessment.value?.operasi)?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "riwayat_penyakit" to assessment.value?.riwayat_penyakit?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "riwayat_alergi" to assessment.value?.riwayat_alergi?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "keluhan" to assessment.value?.keluhan?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "id_pasien" to idPasien?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "foto" to assessment.value?.foto?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "id_jadwal_konsultasi" to id?.toRequestBody("text/plain".toMediaTypeOrNull()),
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postAssessment(
                param2,
                getMultipartImage(),
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
                    setStatus(
                        hashMapOf(
                            "status" to "success",
                            "msg" to it
                        )
                    )
                }
            )
        }
    }

    var fileToUpload: MultipartBody.Part? = null

    fun setPhoto(file: File) {
        // Parsing any Media type file
        val requestBody: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        fileToUpload =
            MultipartBody.Part.createFormData("foto", file.name, requestBody)
        Timber.d("requestBody = $fileToUpload")
    }

    private fun updateAssessment2(idPasien: String?, id: String?) {
        setLoading(true)
        val bp1 = if(bloodPressure1.value.isNullOrEmpty()) {
            "0"
        } else {
            bloodPressure1.value
        }
        val bp2 = if(bloodPressure2.value.isNullOrEmpty()) {
            "0"
        } else {
            bloodPressure2.value
        }
        val bloodPressure = "$bp1/$bp2"
        Timber.d("blodpresall: $bloodPressure")
        val param2 = hashMapOf<String?, RequestBody?>(
            "id_jadwal_konsultasi" to id?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "berat_badan" to assessment.value?.berat_badan?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "tinggi_badan" to assessment.value?.tinggi_badan?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "tekanan_darah" to bloodPressure.toRequestBody("text/plain".toMediaTypeOrNull()),
            "suhu" to assessment.value?.suhu?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "merokok" to radioButtonValue(assessment.value?.merokok).toRequestBody("text/plain".toMediaTypeOrNull()),
            "alkohol" to radioButtonValue(assessment.value?.alkohol).toRequestBody("text/plain".toMediaTypeOrNull()),
            "kecelakaan" to radioButtonValue(assessment.value?.kecelakaan).toRequestBody("text/plain".toMediaTypeOrNull()),
            "dirawat" to radioButtonValue(assessment.value?.dirawat).toRequestBody("text/plain".toMediaTypeOrNull()),
            "operasi" to radioButtonValue(assessment.value?.operasi).toRequestBody("text/plain".toMediaTypeOrNull()),
            "riwayat_penyakit" to assessment.value?.riwayat_penyakit?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "riwayat_alergi" to assessment.value?.riwayat_alergi?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "keluhan" to assessment.value?.keluhan?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "id_pasien" to idPasien?.toRequestBody("text/plain".toMediaTypeOrNull())
        )
        Timber.d(param2.toString())
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateAssessment(
                param2,
                getMultipartImage(),
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
                    setStatus(
                        hashMapOf(
                            "status" to "success",
                            "msg" to it
                        )
                    )
                }
            )
        }
    }

    fun addImage(uri: Uri) {
        images.add(uri)
        hasSelectImage = true
    }

    fun getImage(index: Int): Uri? {
        if (index < images.size) {
            return images[index]
        }
        return null
    }


    private fun getMultipartImage(): List<MultipartBody.Part> {
        if (!hasSelectImage) {
            return listOf()
        }
        val imagesToUpload = mutableListOf<MultipartBody.Part>()
        for(i in 0 until images.size) {
            val file = images[i].toFile()
            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("foto[]", file.name, requestBody)
            imagesToUpload.add(multipartBody)
        }
        return imagesToUpload
    }
}