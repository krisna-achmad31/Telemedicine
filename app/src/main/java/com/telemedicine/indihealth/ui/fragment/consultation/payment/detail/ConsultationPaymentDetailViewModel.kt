package com.telemedicine.indihealth.ui.fragment.consultation.payment.detail

import android.text.Editable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.Payment
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ConsultationRepository
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
import java.text.SimpleDateFormat
import java.util.*

class ConsultationPaymentDetailViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    private val user: User? by lazy {
        sharedPreference.getUserValue()
    }

    var payment: Payment? = null
    var fileToUpload: MultipartBody.Part? = null
    var param: HashMap<String?, RequestBody?>? = null



    val birthDate: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = sharedPreference.getUserValue()?.getTanggalLahir
            Timber.d("${sharedPreference.getUserValue()?.lahir_tanggal} tanggal Lahir ${sharedPreference.getUserValue()?.getTanggalLahir}")
        }
    }

    val cardNumber: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val fullName: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = sharedPreference.getUserValue()?.name
        }
    }

    val isFieldsFilled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isFieldsFilledOtp: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    //verification
    val otp: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    //verification obat

    val id_jadwal_konsultasi: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    fun setPhoto(file: File) {
        // Parsing any Media type file
        val requestBody: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        fileToUpload =
            MultipartBody.Part.createFormData("foto", file.name, requestBody)
        Timber.d("requestBody = $fileToUpload")
    }

    @Suppress("UNUSED_PARAMETER")
    fun afterTextChanged(e: Editable) {
        isFieldsFilled.value =
            !(fullName.value.isNullOrBlank() || birthDate.value.isNullOrBlank() ||
                    cardNumber.value.isNullOrBlank() || otp.value.isNullOrBlank())

        isFieldsFilledOtp.value =
            !(fullName.value.isNullOrBlank() || birthDate.value.isNullOrBlank() ||
                    cardNumber.value.isNullOrBlank())
        Timber.d("isFieldFilled? = ${isFieldsFilled.value}")

    }

    fun postPayment() {
        setLoading(true)
        Timber.d("payment.id = ${payment?.id}")
        Timber.d("payment.id_pasien = ${payment?.id_pasien}")
        param = hashMapOf(
            "id_registrasi" to payment?.id?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "id_pasien" to payment?.id_pasien?.toRequestBody("text/plain".toMediaTypeOrNull()),
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postPayment(
                param!!,
                fileToUpload,
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

    fun postPaymentOwlexagenerateOtp() {
        setLoading(true)
        Timber.d("payment.tanggal_lahir= ${payment?.tanggal_lahir}")
        Timber.d("payment.card_number = ${payment?.card_number}")
        Timber.d("payment.nama_lengkap = ${payment?.nama_lengkap}")

        val param:HashMap<String?, String?> = hashMapOf(
            "birthDate" to convertDate(birthDate.value!!),
            "cardNumber" to cardNumber.value!!,
            "fullName" to fullName.value!!,
            "providerCode" to "3495",
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postPaymentOwlexaGenerateOtp(
                param,
                onSuccess = {
                    setLoading(false)
                    setStatus(
                        hashMapOf(
                            "status" to "success",
                            "msg" to it,
                            "title" to "Generate OTP",
                            "isNeedBack" to true
                        )
                    )
                },
                onError = {
                    setLoading(false)
                    setStatus(
                        hashMapOf(
                            "status" to "failed",
                            "msg" to it,
                            "title" to "Generate OTP"
                        )
                    )
                }
            )
        }
    }

    fun postPaymentOwlexaVerification() {
        setLoading(true)
        //date
        val c: Date = Calendar.getInstance().time

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val admissionDate: String = df.format(c)

        val param:HashMap<String?, String?> = hashMapOf(
            "birthDate" to convertDate(birthDate.value!!),
            "cardNumber" to cardNumber.value!!,
            "fullName" to fullName.value!!,
            "providerCode" to "3494",
            "admissionDate" to admissionDate,
            "chargeValue" to payment?.harga,
            "otp" to otp.value!!,
            "telemedicineType" to "TM-001", //TM-001 untuk konsultasi, TM-002 untuk obat
            "id_pasien" to payment?.id_pasien,
            "id_dokter" to payment?.id_dokter,
            "id_registrasi" to payment?.id

        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postPaymentOwlexaVerification(
                param,
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

    private fun convertDate(date : String) : String{
        val temp2 = date.split("-")
        return temp2[2]+"-"+temp2[1]+"-"+temp2[0]
    }

    fun getTocOwlexa(){
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO){
            mRepository
                .getTocOwlexa(
                    onSuccess = {
                        setLoading(false)
                        setStatus(
                            hashMapOf(
                                "status" to "SUCCESSTOC",
                                "msg" to it
                            )
                        )
                    },
                    onError = {
                        setLoading(false)
                        setStatus(
                            hashMapOf(
                                "title" to "Gagal Terhubung ke Server",
                                "status" to "failed",
                                "msg" to it
                            )
                        )
                    }
                )
        }
    }

    fun resetData() {
        cardNumber.value = ""
        isFieldsFilled.value = false
        isFieldsFilledOtp.value = false
        otp.value = ""
    }


}