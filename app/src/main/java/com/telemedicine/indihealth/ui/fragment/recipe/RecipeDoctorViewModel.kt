package com.telemedicine.indihealth.ui.fragment.recipe

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.Recipe
import com.telemedicine.indihealth.network.repo.RecipeRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class RecipeDoctorViewModel @ViewModelInject constructor(
    private val mRepository: RecipeRepository,
    private val sharedPreference: SharedPreferenceApp

) : BaseViewModel() {


    val profileDoctorList: MutableLiveData<List<Recipe>> by lazy {
        MutableLiveData<List<Recipe>>()
    }

    val isProfileDoctorListExist: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    fun setProfileDoctorListExist(boolean: Boolean?) {
        isProfileDoctorListExist.value = boolean
    }

    var polyDoctorList: List<String?> = mutableListOf()
    var idDcoctorList: List<String?> = mutableListOf()
    var poli: String? = ""
    var positionPoly: Int = 0
    val polyList: MutableLiveData<List<String?>> by lazy {
        MutableLiveData<List<String?>>()
    }

    var statusList: List<String?> = mutableListOf()
    var statusPayment: String? = ""
    var choice: String? = ""
    var positionStatusPayment: Int = 0
    val statusPaymentList: MutableLiveData<List<String?>> by lazy {
        MutableLiveData<List<String?>>()
    }

    private fun setProfileDoctorListAsync(it: List<Recipe>?) {
        val tempStatus : ArrayList<String?> = ArrayList()
        var stat: String? = "0"
        var ind: Int = 0
        for (value in it!!) {
            if (value.getStatusPembayaran != "1") {
                stat = "0"
                for (row in idDcoctorList) {
                    if (row == value.id_dokter) {
                        stat = "1"
                    }
                }
                if (stat == "0") {
                    polyDoctorList += value.poli
                    idDcoctorList += value.id_dokter
//                    if (poli == "") {
//                        if (ind == 0) {
//                            poli = value.poli
//                            ind += 1
//                        }
//                    }
                }
//                stat = "0"
//                for (row in statusList) {
//                    if (row == value.getStatusPembayaran) {
//                        stat = "1"
//                    }
//                }
//                if (stat == "0") {
//                }
            }
        }
        for(value in it){
            tempStatus.add(value.getStatusPembayaran)
        }
        var temp: List<Recipe> = mutableListOf()
        for (value in it) {
            if(poli != ""){
                if (value.poli == poli) {
                    if (statusPayment != "") {
                        if (statusPayment == value.getStatusPembayaran) {
                            temp += value
                        }
                    } else {
                        temp += value
                    }
                }
            } else {
                if (statusPayment != "") {
                    if (statusPayment == value.getStatusPembayaran) {
                        temp += value
                    }
                } else {
                    temp += value
                }
            }
        }
        profileDoctorList.postValue(temp)
        polyList.postValue(polyDoctorList)
        statusList = tempStatus.distinct()
        statusPaymentList.postValue(statusList)
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }

    fun getConsultationDoctor() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getRecipe(
                id_pasien = sharedPreference.getUserValue()?.id,
                onSuccess = {
                    setLoading(false)
                    setProfileDoctorListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setProfileDoctorListAsync(listOf())
                }
            )
        }
    }

    fun initConsultationDoctor() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getRecipe(
                id_pasien = sharedPreference.getUserValue()?.id,
                onSuccess = {
                    setLoading(false)
                    setProfileDoctorListAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                    setProfileDoctorListAsync(listOf())
                }
            )
        }
    }

    var parameters: HashMap<String?, Any?> = hashMapOf()

    fun deletePayment() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deletePayment(
                parameters,
                onSuccess = {
                    initConsultationDoctor()
                },
                onError = {
                }
            )
        }
    }

    fun cancelRecipe() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.cancelRecipe(
                parameters,
                onSuccess = {
                    initConsultationDoctor()
                },
                onError = {
                }
            )
        }
    }


}