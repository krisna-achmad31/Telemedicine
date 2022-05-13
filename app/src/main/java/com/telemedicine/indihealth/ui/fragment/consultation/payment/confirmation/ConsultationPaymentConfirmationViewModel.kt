package com.telemedicine.indihealth.ui.fragment.consultation.payment.confirmation

import android.text.Editable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.*
import com.telemedicine.indihealth.network.repo.ConsultationRepository
import com.telemedicine.indihealth.network.repo.ProfileRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.util.*

class ConsultationPaymentConfirmationViewModel @ViewModelInject constructor(
    private val mRepository: ConsultationRepository,
    private val sharedPreference: SharedPreferenceApp,
    private val profileRepository: ProfileRepository,
) : BaseViewModel() {

    var address_city: String = ""

    var address_province: String = ""

    var address_subdistrict: String = ""

    var address_subdistrict2: String = ""

    private val user: User? by lazy {
        sharedPreference.getUserValue()
    }

    var param = hashMapOf<String?, Any?>()

    val provinsi: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val kabupaten: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val kelurahan: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val kecamatan: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val kodepos: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val phone: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val alamat: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val paymentId: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = sharedPreference.getUserValue()?.email
        }
    }

    val isFieldsFilled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    var listPaymetnMethod: ListPaymetnMethod? = null
    var payment: Payment? = null
    var recipe: Recipe? = null
    var result: PaymentResult? = null
    var shipmentAddress: ShipmentAddress? = null

    @Suppress("UNUSED_PARAMETER")
    fun afterTextChanged(e: Editable) {
        isFieldsFilled.value =
            !(provinsi.value.isNullOrBlank() || kabupaten.value.isNullOrBlank() ||
                    kecamatan.value.isNullOrBlank() || kelurahan.value.isNullOrBlank() ||
                    kodepos.value.isNullOrBlank() || alamat.value.isNullOrBlank())

        Timber.d("isFieldFilled? = ${isFieldsFilled.value}")

    }

    fun afterSpinnerChanged(){
        isFieldsFilled.value =
            !(provinsi.value.isNullOrBlank() || kabupaten.value.isNullOrBlank() ||
                    kecamatan.value.isNullOrBlank() || kelurahan.value.isNullOrBlank() ||
                    kodepos.value.isNullOrBlank() || alamat.value.isNullOrBlank())

        Timber.d("isFieldFilled? = ${isFieldsFilled.value}")
    }

    fun setShipmentAddress() {
        shipmentAddress = ShipmentAddress(
            "${provinsi.value}",
            "${kabupaten.value}",
            "${kecamatan.value}",
            "${kelurahan.value}",
            "${alamat.value}",
            "${kodepos.value}"
        )
    }

    var countAddress: Int = 0

    val provinsiList: MutableLiveData<List<GlobalItem>> by lazy {
        MutableLiveData<List<GlobalItem>>()
    }

    fun getProvinsi() {
//        provinsi.postValue("")
//        kabupaten.postValue("")
//        kecamatan.postValue("")
//        provinsi.postValue("")
        val param = hashMapOf<String, String?>()
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.getProvinsi(
                param,
                onSuccess = {
                    it?.let {
                        val list = mutableListOf<GlobalItem>()
                        list.add(GlobalItem(name = "Pilih Provinsi"))
                        for (globalItem in it) {
                            list.add(globalItem)
                        }
                        provinsiList.postValue(list)
                        countAddress += 1
                    }
                },
                onError = {
                    provinsiList.postValue(listOf())
                }
            )
        }
    }

    val kotaList: MutableLiveData<List<GlobalItem>> by lazy {
        MutableLiveData<List<GlobalItem>>()
    }

    fun getKota(string: String?) {
        val param = hashMapOf<String, String?>(
            "id_provinsi" to string
        )
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.getKota(
                param,
                onSuccess = {
                    it?.let {
                        val list = mutableListOf<GlobalItem>()
                        list.add(GlobalItem(name = "Pilih Kota"))
                        for (globalItem in it) {
                            list.add(globalItem)
                        }
                        kotaList.postValue(list)
                    }
                },
                onError = {
                    kotaList.postValue(listOf())
                }
            )
        }
    }

    val kecamatanList: MutableLiveData<List<GlobalItem>> by lazy {
        MutableLiveData<List<GlobalItem>>()
    }

    fun getKecamatan(string: String?) {
        val param = hashMapOf<String, String?>(
            "id_kota" to string
        )
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.getKecamatan(
                param,
                onSuccess = {
                    it?.let {
                        val list = mutableListOf<GlobalItem>()
                        list.add(GlobalItem(name = "Pilih Kecamatan"))
                        for (globalItem in it) {
                            list.add(globalItem)
                        }
                        kecamatanList.postValue(list)
                    }
                },
                onError = {
                    kecamatanList.postValue(listOf())
                }
            )
        }
    }

    val kelurahanList: MutableLiveData<List<GlobalItem>> by lazy {
        MutableLiveData<List<GlobalItem>>()
    }

    fun getKelurahan(string: String?) {
        val param = hashMapOf<String, String?>(
            "id_kecamatan" to string
        )
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.getKelurahan(
                param,
                onSuccess = {
                    it?.let {
                        val list = mutableListOf<GlobalItem>()
                        list.add(GlobalItem(name = "Pilih Kelurahan"))
                        for (globalItem in it) {
                            list.add(globalItem)
                        }
                        kelurahanList.postValue(list)
                    }
                },
                onError = {
                    kelurahanList.postValue(listOf())
                }
            )
        }
    }

    private fun setProfileAsync(it: User?) {
        kodepos.postValue(it?.kode_pos)
        alamat.postValue(it?.alamat_jalan)
        if (!it?.alamat_provinsi.isNullOrEmpty()) {
            address_province = it?.alamat_provinsi.toString()
            address_city = it?.alamat_kota.toString()
            address_subdistrict = it?.alamat_kecamatan.toString()
            address_subdistrict2 = it?.alamat_kelurahan.toString()
            provinsi.postValue(it?.alamat_provinsi.toString())
            kabupaten.postValue(it?.alamat_kota.toString())
            kecamatan.postValue(it?.alamat_kecamatan.toString())
            kelurahan.postValue(it?.alamat_kelurahan.toString())
            var list = mutableListOf<GlobalItem>()
            list.add(GlobalItem(name = "Pilih Provinsi"))
            it?.provinsi?.let { d ->
                list.add(d)
            }
            provinsiList.postValue(list)
            list = mutableListOf<GlobalItem>()
            list.add(GlobalItem(name = "Pilih Kota"))
            it?.kota?.let { d ->
                list.add(d)
            }
            kotaList.postValue(list)
            list = mutableListOf<GlobalItem>()
            list.add(GlobalItem(name = "Pilih Kecamatan"))
            it?.kecamatan?.let { d ->
                list.add(d)
            }
            kecamatanList.postValue(list)
            list = mutableListOf<GlobalItem>()
            list.add(GlobalItem(name = "Pilih Kelurahan"))
            it?.kelurahan?.let { d ->
                list.add(d)
            }
            kelurahanList.postValue(list)
            getProvinsi()
            getKota(address_province)
            getKecamatan(address_city)
            getKelurahan(address_subdistrict)
        } else {
            getProvinsi()
            setLoading(false)
        }

    }

    fun getProfile() {
        setLoading(true)
        val condition: HashMap<String?, String?> =
            hashMapOf("id_pasien" to sharedPreference.getUserValue()?.id)
        Timber.d("id_pasien = ${sharedPreference.getUserValue()?.id}")
        Timber.d("nama_pasien = ${sharedPreference.getUserValue()?.name}")
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.getPatientProfile(
                condition,
                onSuccess = {
                    setLoading(false)
                    setProfileAsync(it)
                },
                onError = {
                    setLoading(false)
                    Timber.d("error = $it")
                }
            )
        }
    }

}