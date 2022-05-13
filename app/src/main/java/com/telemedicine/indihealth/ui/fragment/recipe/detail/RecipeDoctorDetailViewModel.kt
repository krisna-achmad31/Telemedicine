package com.telemedicine.indihealth.ui.fragment.recipe.detail

import android.text.Editable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.Recipe
import com.telemedicine.indihealth.model.RecipeDetail
import com.telemedicine.indihealth.network.repo.RecipeRepository
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

class RecipeDoctorDetailViewModel @ViewModelInject constructor(
    private val mRepository: RecipeRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

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


    var recipe: Recipe? = null

    val recipeList: MutableLiveData<List<RecipeDetail>> by lazy {
        MutableLiveData<List<RecipeDetail>>()
    }

    fun setRecipeDetailAsync(it: List<RecipeDetail>?) {
        recipeList.postValue(it)
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



    fun getRecipe() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getRecipeDetail(
                id_jadwal_konsultasi = recipe?.id_jadwal_konsultasi,
                onSuccess = {
                    setLoading(false)
                    setRecipeDetailAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setRecipeDetailAsync(listOf())
                }
            )
        }
    }

    var param: HashMap<String?, RequestBody?>? = null
    var fileToUpload: MultipartBody.Part? = null

    fun setPhoto(file: File) {
        // Parsing any Media type file
        val requestBody: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        fileToUpload =
            MultipartBody.Part.createFormData("foto", file.name, requestBody)
        Timber.d("requestBody = $fileToUpload")
    }

    fun postPayment() {
        setLoading(true)
        Timber.d("payment.id_jadwal_konsultasi = ${recipe?.id_jadwal_konsultasi}")
        Timber.d("payment.id_pasien = ${recipe?.id_pasien}")
        Timber.d("payment.id_dokter = ${recipe?.id_dokter}")
        param = hashMapOf(
            "id_jadwal_konsultasi" to recipe?.id_jadwal_konsultasi?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "id_pasien" to recipe?.id_pasien?.toRequestBody("text/plain".toMediaTypeOrNull()),
            "id_dokter" to recipe?.id_dokter?.toRequestBody("text/plain".toMediaTypeOrNull())
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
                            "isNeedBack" to false
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


    fun postPaymentOwlexaVerificationObat() {
        setLoading(true)
        val c: Date = Calendar.getInstance().time

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val admissionDate: String = df.format(c)

        val param: java.util.HashMap<String?, String?> = hashMapOf(
            "birthDate" to convertDate(birthDate.value!!),
            "cardNumber" to cardNumber.value!!,
            "fullName" to fullName.value!!,
            "providerCode" to "3494",
            "admissionDate" to admissionDate,
            "chargeValue" to recipe?.harga,
            "otp" to otp.value!!,
            "telemedicineType" to "TM-001", //TM-001 untuk konsultasi, TM-002 untuk obat
            "id_pasien" to recipe?.id_pasien,
            "id_jadwal_konsultasi" to recipe?.id_jadwal_konsultasi
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.postPaymentOwlexaVerificationObat(
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

    fun resetData() {
        cardNumber.value = ""
        isFieldsFilled.value = false
        isFieldsFilledOtp.value = false
        otp.value = ""
    }

}