package com.telemedicine.indihealth.ui.fragment.doctor.profile.edit

import android.text.Editable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.GlobalItem
import com.telemedicine.indihealth.model.User
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DoctorProfileEditViewModel @ViewModelInject constructor(
    private val mRepository: ProfileRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    val profile: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    val fullname: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val telephone: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val birth_place: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val birth_date: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val gender: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val address_street: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val address_city: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val address_province: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val address_subdistrict: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val address_subdistrict2: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val post_code: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val username: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val photo: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    var fileToUpload: MultipartBody.Part? = null

    fun setPhoto(file: File) {
        // Parsing any Media type file
        val requestBody: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        fileToUpload =
            MultipartBody.Part.createFormData("foto", file.name, requestBody)
        Timber.d("requestBody = $fileToUpload")
    }

    val isFieldsFilled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    @Suppress("UNUSED_PARAMETER")
    fun afterTextChanged(e: Editable) {
        isFieldsFilled.value =
            !(fullname.value.isNullOrBlank() || telephone.value.isNullOrBlank() ||
                    birth_place.value.isNullOrBlank() || birth_date.value.isNullOrBlank() ||
                    gender.value.isNullOrBlank() || address_street.value.isNullOrBlank() ||
                    address_city.value.isNullOrBlank() || address_province.value.isNullOrBlank())
        Timber.d("isFieldFilled? = ${isFieldsFilled.value}")
    }

    val address: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    var statGender: Int = 0
    var count: Int = 0

    val userData: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    val genderList: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }
    fun setProfileAsync(user: User?) {
        profile.postValue(user)
        if (user!!.jenis_kelamin == "Perempuan"){
            statGender = 1
        } else {
            statGender = 0
        }


        val list = mutableListOf<String>()
        list.add("Laki-laki")
        list.add("Perempuan")
        genderList.postValue(list)

        fullname.postValue(user.name)
        telephone.postValue(user.telp)
        email.postValue(user.email)
        birth_place.postValue(user.lahir_tempat)
        gender.postValue(user.jenis_kelamin)
        userData.postValue(user)
        password.postValue(user.password)
        username.postValue(user.username)
        post_code.postValue(user.kode_pos)
        address_street.postValue(user.alamat_jalan)
        Timber.d("UsernameEditProfile = ${username.value}")
        photo.postValue(user.foto)
        var dateText : String? = user.lahir_tanggal
//        try {
//            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
//            val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
//            val inputDateStr = it?.lahir_tanggal
//            val date: Date? = inputFormat.parse(inputDateStr)
//            dateText = outputFormat.format(date)
//        }catch (e : Exception){
//
//        }
        birth_date.postValue(dateText)

        if(!user.alamat_provinsi.isNullOrEmpty()){
            address_province.postValue(user.alamat_provinsi)
            address_city.postValue(user.alamat_kota)
            address_subdistrict.postValue(user.alamat_kecamatan)
            address_subdistrict2.postValue(user.alamat_kelurahan)
            val listProvince = mutableListOf<GlobalItem>()
            listProvince.add(GlobalItem(name = "Pilih Provinsi"))
            listProvince.add(user.provinsi!!)
            provinsiList.postValue(listProvince)

            val listCity = mutableListOf<GlobalItem>()
            listCity.add(GlobalItem(name = "Pilih Kota"))
            listCity.add(user.kota!!)
            kotaList.postValue(listCity)

            val subdistrictList = mutableListOf<GlobalItem>()
            subdistrictList.add(GlobalItem(name = "Pilih Kecamatan"))
            subdistrictList.add(user.kecamatan!!)
            kecamatanList.postValue(subdistrictList)
            val subdistrict2List = mutableListOf<GlobalItem>()
            subdistrict2List.add(GlobalItem(name = "Pilih Kelurahan"))
            subdistrict2List.add(user.kelurahan!!)
            kelurahanList.postValue(subdistrict2List)
            getProvinsi()
            getKota(user.alamat_provinsi)
            getKecamatan(user.alamat_kota)
            getKelurahan(user.alamat_kecamatan)
        }else{
            getProvinsi()
        }

        count += 1

    }

    fun getProfile() {
        setLoading(true)
        var condition: HashMap<String?, String?> = hashMapOf("id_dokter" to sharedPreference.getUserValue()?.id)
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getDoctorProfile(
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

    val isEditIn: MutableLiveData<Event<String>> by lazy {
        MutableLiveData<Event<String>>()
    }

    private fun setEditInStatus(string: String) {
        isEditIn.postValue(Event(string))
    }

    var param = hashMapOf<String, RequestBody?>()
    fun attemptEditProfile() {
        var stat: Int = 0
        if (fileToUpload == null) {
            if (photo.value == "") {
                setEditInStatus("tolong pilih foto profil")
                stat = 1
            }
        }
        if (stat == 1) {
        }
        else if (fullname.value == "") {
            setEditInStatus("tolong isi nama lengkap")
        }
        else if (telephone.value == "") {
            setEditInStatus("tolong isi nomor telepon")
        }
        else if (birth_place.value == "") {
            setEditInStatus("tolong isi tempat lahir")
        }
        else if (birth_date.value == "") {
            setEditInStatus("tolong pilih tanggal lahir")
        }
        else if (param["alamat_provinsi"] == null) {
            setEditInStatus("tolong pilih provinsi")
        }
        else if (param["alamat_kota"] == null) {
            setEditInStatus("tolong pilih kota")
        }
        else if (param["alamat_kecamatan"] == null) {
            setEditInStatus("tolong pilih kecamatan")
        }
        else if (param["alamat_kelurahan"] == null) {
            setEditInStatus("tolong pilih kelurahan")
        }
        else if (address_street.value == "") {
            setEditInStatus("tolong isi alamat jalan")
        }
        else {
            setLoading(true)
            val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputDateStr = birth_date.value
            val date: Date = inputFormat.parse(inputDateStr)
            val outputDateStr: String = outputFormat.format(date)

            Timber.d("password = ${password.value}")
            Timber.d("user = ${username.value}")
            param["name"] = fullname.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["jenis_kelamin"] = gender.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["lahir_tempat"] =
                birth_place.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["lahir_tanggal"] =
                outputDateStr.toRequestBody("text/plain".toMediaTypeOrNull())
            param["alamat_jalan"] =
                address_street.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            //        param["kode_pos"] = post_code.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["telp"] = telephone.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["username"] = username.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["email"] = email.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            Timber.d("file to upload = $fileToUpload")
            Timber.d("param = $param")
            viewModelScope.launch(Dispatchers.IO) {
                mRepository.postEditProfile(
                    param,
                    fileToUpload,
                    onSuccess = {
                        setLoading(false)
                        Timber.d("User it = $it")
                        Timber.d("User = ${sharedPreference.getUserValue()}")
                        setEditInStatus("success")
                    },
                    onError = {
                        setLoading(false)
                        if (it == "user gagal diupdate.") {
                            setEditInStatus("tidak ada perubahan data")
                        } else {
                            setEditInStatus(it!!)
                        }
                    }
                )
            }
        }
    }

    var countAddress: Int = 0

    val provinsiList: MutableLiveData<List<GlobalItem>> by lazy {
        MutableLiveData<List<GlobalItem>>()
    }
    val provinsi: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getProvinsi() {
        val param = hashMapOf<String, String?>()
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getProvinsi(
                param,
                onSuccess = {
                    it?.let {
                        val list = mutableListOf<GlobalItem>()
                        list.add(GlobalItem(name = "Pilih Provinsi"))
                        for (globalItem in it) {
                            list.add(globalItem)
                        }
                        provinsiList.postValue(list)
                        /*if (countAddress == 0) {
                        }*/
                        countAddress = countAddress + 1
//                        isProvinceFetched.postValue(true)
//                        stopLoading()
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
    val kota: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getKota(string: String?) {
        val param = hashMapOf<String, String?>(
            "id_provinsi" to string
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getKota(
                param,
                onSuccess = {
                    it?.let {
                        val list = mutableListOf<GlobalItem>()
                        list.add(GlobalItem(name = "Pilih Kota"))
                        for (globalItem in it) {
                            list.add(globalItem)
                        }
                        kotaList.postValue(list)
//                        isCityFetched.postValue(true)
//                        stopLoading()
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
    val kecamatan: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getKecamatan(string: String?) {
        val param = hashMapOf<String, String?>(
            "id_kota" to string
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getKecamatan(
                param,
                onSuccess = {
                    it?.let {
                        val list = mutableListOf<GlobalItem>()
                        list.add(GlobalItem(name = "Pilih Kecamatan"))
                        for (globalItem in it) {
                            list.add(globalItem)
                        }
                        kecamatanList.postValue(list)
//                        isDistrictFetched.postValue(true)
//                        stopLoading()
                    }
                },
                onError = {
                    kecamatanList.postValue(listOf()) }
            )
        }
    }

    val kelurahanList: MutableLiveData<List<GlobalItem>> by lazy {
        MutableLiveData<List<GlobalItem>>()
    }
    val kelurahan: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getKelurahan(string: String?) {
        val param = hashMapOf<String, String?>(
            "id_kecamatan" to string
        )
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getKelurahan(
                param,
                onSuccess = {
                    it?.let {
                        val list = mutableListOf<GlobalItem>()
                        list.add(GlobalItem(name = "Pilih Kelurahan"))
                        for (globalItem in it) {
                            list.add(globalItem)
                        }
                        kelurahanList.postValue(list)
//                        isSubDistrictFetched.postValue(true)
//                        stopLoading()
                    }
                },
                onError = {
                    kelurahanList.postValue(listOf())
                }
            )
        }
    }
}