package com.telemedicine.indihealth.ui.fragment.profile.patient.edit

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

class ProfileEditViewModel @ViewModelInject constructor(
    private val mRepository: ProfileRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    val nik: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val fullname: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val telephone: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    // ===== Penanggung Jawab =======

    val nama_ibu: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val nama_ayah: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val nama_pj: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val hubungan_pj: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val telephone_pj: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val address_street_pj: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val post_code_pj: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    // ===== Penanggung Jawab =======

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

    var address_city: String = ""

    var address_province: String = ""

    var address_subdistrict: String = ""

    var address_subdistrict2: String = ""

    // ===== Penanggung Jawab =======

    var address_city_pj: String = ""

    var address_province_pj: String = ""

    var address_subdistrict_pj: String = ""

    var address_subdistrict2_pj: String = ""

    // ===== Penanggung Jawab =======

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

    var statGender: Int = 0
    var count: Int = 0

    val userData: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    val genderList: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    private fun setProfileAsync(it: User?) {
        nik.postValue(it?.nik)
        fullname.postValue(it?.name)
        telephone.postValue(it?.telp)
        nama_ayah.postValue(it?.nama_ayah)
        nama_ibu.postValue(it?.nama_ibu)
        nama_pj.postValue(it?.nama_pj)
        hubungan_pj.postValue(it?.hubungan_pj)
        telephone_pj.postValue(it?.telp_pj)
        address_street_pj.postValue(it?.alamat_jalan_pj)
        post_code_pj.postValue(it?.kode_pos_pj)
        email.postValue(it?.email)
        birth_place.postValue(it?.lahir_tempat)
        gender.postValue(it?.jenis_kelamin)
        photo.postValue(it?.foto)
        userData.postValue(it)
        birth_date.postValue(it?.lahir_tanggal)
        post_code.postValue(it?.kode_pos)
        address_street.postValue(it?.alamat_jalan)

        if (!it?.alamat_provinsi.isNullOrEmpty()) {
            address_province = it?.alamat_provinsi.toString()
            address_city = it?.alamat_kota.toString()
            address_subdistrict = it?.alamat_kecamatan.toString()
            address_subdistrict2 = it?.alamat_kelurahan.toString()
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

        //GET PROV TO SPINNER
        if (!it?.alamat_provinsi_pj.isNullOrEmpty()) {
            address_province_pj = it?.alamat_provinsi_pj.toString()
            address_city_pj = it?.alamat_kota_pj.toString()
            address_subdistrict_pj = it?.alamat_kecamatan_pj.toString()
            address_subdistrict2_pj = it?.alamat_kelurahan_pj.toString()
            var list = mutableListOf<GlobalItem>()
            list.add(GlobalItem(name= "Pilih Provinsi"))
            it?.provinsi_pj?.let { d ->
                list.add(d)
            }
            provinsiListPj.postValue(list)
            list = mutableListOf<GlobalItem>()
            list.add(GlobalItem(name = "Pilih Kota"))
            it?.kota_pj?.let { d ->
                list.add(d)
            }
            kotaListPj.postValue(list)
            list = mutableListOf<GlobalItem>()
            list.add(GlobalItem(name = "Pilih Kecamatan"))
            it?.kecamatan_pj?.let { d ->
                list.add(d)
            }
            kecamatanListPj.postValue(list)
            list = mutableListOf<GlobalItem>()
            list.add(GlobalItem(name = "Pilih Kelurahan"))
            it?.kelurahan_pj?.let { d ->
                list.add(d)
            }
            kelurahanListPj.postValue(list)
            getProvinsiPj()
            getKotaPj(address_province_pj)
            getKecamatanPj(address_city_pj)
            getKelurahanPj(address_subdistrict_pj)
        } else {
            getProvinsiPj()
            setLoading(false)
        }

//-------------------------------------------//

//        if (!it?.alamat_provinsi_pj.isNullOrEmpty()) {
//            address_province_pj = it?.alamat_provinsi_pj.toString()
//            address_city_pj = it?.alamat_kota_pj.toString()
//            address_subdistrict_pj = it?.alamat_kecamatan_pj.toString()
//            address_subdistrict2_pj = it?.alamat_kelurahan_pj.toString()

        //            var list = mutableListOf<GlobalItem>()
//            list.add(GlobalItem(name = "Pilih Provinsi"))
//            it?.provinsi_pj?.let { d ->
//                list.add(d)
//            }
//            provinsiList.postValue(list)
//            list = mutableListOf<GlobalItem>()
//            list.add(GlobalItem(name = "Pilih Kota"))
//            it?.kota_pj?.let { d ->
//                list.add(d)
//            }
//            kotaList.postValue(list)
//            list = mutableListOf<GlobalItem>()
//            list.add(GlobalItem(name = "Pilih Kecamatan"))
//            it?.kecamatan_pj?.let { d ->
//                list.add(d)
//            }
//            kecamatanList.postValue(list)
//            list = mutableListOf<GlobalItem>()
//            list.add(GlobalItem(name = "Pilih Kelurahan"))
//            it?.kelurahan_pj?.let { d ->
//                list.add(d)
//            }
//            kelurahanList.postValue(list)
//            getProvinsi()
//            getKota(address_province_pj)
//            getKecamatan(address_city_pj)
//            getKelurahan(address_subdistrict_pj)

        //} else {

//            getProvinsi()
//            setLoading(false)

        //}

//        if (count == 0) {
//            post_code.postValue(it?.kode_pos)
//        }
//        if (count == 0) {
//            address_street.postValue(it?.alamat_jalan)
//        }
//        if (count == 0) {
//            address_city = it?.alamat_kota.toString()
//        }
//        if (count == 0) {
//            address_province = it?.alamat_provinsi.toString()
//        }
//        if (count == 0) {
//            address_subdistrict = it?.alamat_kecamatan.toString()
//        }
//        if (count == 0) {
//            address_subdistrict2 = it?.alamat_kelurahan.toString()
//        }

        statGender = if (it?.jenis_kelamin == "Perempuan") {
            1
        } else {
            0
        }

        password.postValue(it?.password)
        username.postValue(it?.username)

        val list = mutableListOf<String>()
        list.add("Laki-laki")
        list.add("Perempuan")
        genderList.postValue(list)

        count += 1
    }

    fun getProfile() {
        setLoading(true)
        val condition: HashMap<String?, String?> =
            hashMapOf("id_pasien" to sharedPreference.getUserValue()?.id)
        Timber.d("id_pasien = ${sharedPreference.getUserValue()?.id}")
        Timber.d("nama_pasien = ${sharedPreference.getUserValue()?.name}")
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getPatientProfile(
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
            !(nik.value.isNullOrBlank() || fullname.value.isNullOrBlank() || nama_ibu.value.isNullOrBlank() || nama_ayah.value.isNullOrBlank()
                    || nama_pj.value.isNullOrBlank() || hubungan_pj.value.isNullOrBlank()
                    || telephone_pj.value.isNullOrBlank() || address_street_pj.value.isNullOrBlank()
                    || telephone.value.isNullOrBlank() || email.value.isNullOrBlank() ||
                    birth_place.value.isNullOrBlank() || birth_date.value.isNullOrBlank() ||
                    gender.value.isNullOrBlank() || address_street.value.isNullOrBlank() ||
                    address_city.isEmpty() || address_province.isEmpty() || address_province_pj.isEmpty() || address_city_pj.isEmpty())
        Timber.d("isFieldFilled? = ${isFieldsFilled.value}")
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
        } else if (nik.value == "") {
            setEditInStatus("tolong isi nik lengkap")
        } else if (fullname.value == "") {
            setEditInStatus("tolong isi nama lengkap")
        } else if (nama_ibu.value == "") {
            setEditInStatus("tolong isi nama ibu")
        } else if (nama_ayah.value == "") {
            setEditInStatus("tolong isi nama ayah")
        } else if (nama_pj.value == "") {
            setEditInStatus("tolong isi nama Penanggung Jawab")
        } else if (hubungan_pj.value == "") {
            setEditInStatus("tolong isi hubungan penanggung jawab")
        } else if (telephone_pj.value == "") {
            setEditInStatus("tolong isi nomer telp penanggung jawab")
        } else if (address_street_pj.value == "") {
            setEditInStatus("tolong isi alamat jalan penanggung jawab")
        } else if (post_code_pj.value == "") {
            setEditInStatus("tolong isi kode pos penanggung jawab")
//        } else if (param["alamat_provinsi_pj"] == null) {
//            setEditInStatus("tolong pilih provinsi penanggung jawab")
//        } else if (param["alamat_kota_pj"] == null) {
//            setEditInStatus("tolong pilih kota penanggung jawab")
//        } else if (param["alamat_kecamatan_pj"] == null) {
//            setEditInStatus("tolong pilih kecamatan penanggung jawab")
//        } else if (param["alamat_kelurahan_pj"] == null) {
//            setEditInStatus("tolong pilih kelurahan penanggung jawab")
        } else if (telephone.value == "") {
            setEditInStatus("tolong isi nomor telepon")
        } else if (email.value == "") {
            setEditInStatus("tolong isi email")
        } else if (birth_place.value == "") {
            setEditInStatus("tolong isi tempat lahir")
        } else if (birth_date.value == "") {
            setEditInStatus("tolong pilih tanggal lahir")
        } else if (param["alamat_provinsi"] == null) {
            setEditInStatus("tolong pilih provinsi")
        } else if (param["alamat_kota"] == null) {
            setEditInStatus("tolong pilih kota")
        } else if (param["alamat_kecamatan"] == null) {
            setEditInStatus("tolong pilih kecamatan")
        } else if (param["alamat_kelurahan"] == null) {
            setEditInStatus("tolong pilih kelurahan")
        } else if (address_street.value == "") {
            setEditInStatus("tolong isi alamat jalan")
        } else if (post_code.value == "") {
            setEditInStatus("tolong isi kode pos")
        } else {
            setLoading(true)
            val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputDateStr = birth_date.value
            val date: Date = inputFormat.parse(inputDateStr)
            val outputDateStr: String = outputFormat.format(date)

            Timber.d("password = ${password.value}")
            Timber.d("user = ${username.value}")

            param["nik"] = nik.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["name"] = fullname.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["nama_ibu"] = nama_ibu.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["nama_ayah"] = nama_ayah.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["nama_pj"] = nama_pj.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["hubungan_pj"] = hubungan_pj.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["jenis_kelamin"] = gender.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["email"] = email.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["lahir_tempat"] = birth_place.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["lahir_tanggal"] = outputDateStr.toRequestBody("text/plain".toMediaTypeOrNull())
            param["alamat_jalan"] = address_street.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["alamat_jalan_pj"] = address_street_pj.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["kode_pos"] = post_code.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["kode_pos_pj"] = post_code_pj.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["telp"] = telephone.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["telp_pj"] = telephone_pj.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            param["username"] = username.value?.toRequestBody("text/plain".toMediaTypeOrNull())
//            param["alamat_kelurahan_pj"] = address_subdistrict2_pj.toRequestBody("text/plain".toMediaTypeOrNull())
//            param["alamat_kecamatan_pj"] = address_subdistrict_pj.toRequestBody("text/plain".toMediaTypeOrNull())
//            param["alamat_kota_pj"] = address_city_pj.toRequestBody("text/plain".toMediaTypeOrNull())
//            param["alamat_provinsi_pj"] = address_province_pj.toRequestBody("text/plain".toMediaTypeOrNull())
//            param["alamat_provinsi"] = address_province.toRequestBody("text/plain".toMediaTypeOrNull())
            Timber.d("file to upload = $fileToUpload")
            Timber.d("param = $param")
            viewModelScope.launch(Dispatchers.IO) {
                mRepository.postEditProfile(
                    param,
                    fileToUpload,
                    onSuccess = {
                        setLoading(false)
                        Timber.d("User it = $it")
                        it?.let { sharedPreference.setUserValue(it) }
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
                        countAddress += 1
                    }
                },
                onError = {
                    provinsiList.postValue(listOf())
                }
            )
        }
    }

    //Get provinsi PJ
    var countAddressPj: Int = 0

    val provinsiListPj: MutableLiveData<List<GlobalItem>> by lazy {
        MutableLiveData<List<GlobalItem>>()
    }
    val provinsi_pj: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getProvinsiPj() {
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
                        provinsiListPj.postValue(list)
                        countAddressPj += 1
                    }
                },
                onError = {
                    provinsiListPj.postValue(listOf())
                }
            )
        }
    }
    ///------------------------------------////

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
                    }
                },
                onError = {
                    kotaList.postValue(listOf())
                }
            )
        }
    }

    //GET KOTA PJ
    val kotaListPj: MutableLiveData<List<GlobalItem>> by lazy {
        MutableLiveData<List<GlobalItem>>()
    }
    val kota_pj: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getKotaPj(string: String?) {
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
                        kotaListPj.postValue(list)
                    }
                },
                onError = {
                    kotaListPj.postValue(listOf())
                }
            )
        }
    }
    //------------------------------------------------//
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
                    }
                },
                onError = {
                    kecamatanList.postValue(listOf())
                }
            )
        }
    }

    //GET KECEMATAN PJ
    val kecamatanListPj: MutableLiveData<List<GlobalItem>> by lazy {
        MutableLiveData<List<GlobalItem>>()
    }
    val kecamatan_pj: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getKecamatanPj(string: String?) {
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
                        kecamatanListPj.postValue(list)

                    }
                },
                onError = {
                    kecamatanListPj.postValue(listOf())
                }
            )
        }
    }
//-------------------------------------------//
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
                    }
                },
                onError = {
                    kelurahanList.postValue(listOf())
                }
            )
        }
    }

    //GET KELURAHAN PJ
    val kelurahanListPj: MutableLiveData<List<GlobalItem>> by lazy {
        MutableLiveData<List<GlobalItem>>()
    }
    val kelurahan_pj: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getKelurahanPj(string: String?) {
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
                        kelurahanListPj.postValue(list)
                    }
                },
                onError = {
                    kelurahanListPj.postValue(listOf())
                }
            )
        }
    }
    //-------------------------------------//
}