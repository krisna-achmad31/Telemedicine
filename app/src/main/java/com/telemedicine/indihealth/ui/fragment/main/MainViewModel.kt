package com.telemedicine.indihealth.ui.fragment.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.model.MenuKonsultasi
import com.telemedicine.indihealth.model.News
import com.telemedicine.indihealth.model.Notification
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.MainRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    private val TAG="com.telemedicine.indihealth.ui.fragment.main.MainViewModel"


    init {
        getNews()
    }

    val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().apply {
            value = sharedPreference.getUserValue()
        }
    }

    val userName: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = "Hai, ${user.value?.name}"
        }
    }

    val menuKonsultasi: MutableLiveData<List<MenuKonsultasi>> by lazy {
        MutableLiveData<List<MenuKonsultasi>>().apply {
            value = listOf(
                MenuKonsultasi(
                    "daftar",
                    "1. Pendaftaran",
                    R.drawable.pendaftaran_icon
                ),
                MenuKonsultasi(
                    "jadwal_terdaftar",
                    "2. Cek Status Pendaftaran",
                    R.drawable.status_pendaftaran_icon
                ),
                MenuKonsultasi(
                    "assessment",
                    "3. Isi Assessment",
                    R.drawable.isi_assessment_icon
                ),
                MenuKonsultasi(
                    "jadwal_telekonsultasi",
                    "4. Jadwal Telekonsultasi",
                    R.drawable.jadwal_telekonsultasi_icon
                )
            )
        }
    }
    val menuLainnya: MutableLiveData<List<MenuKonsultasi>> by lazy {
        MutableLiveData<List<MenuKonsultasi>>().apply {
            value = listOf(
//                MenuKonsultasi(
//                    "data_pasien",
//                    "Data Pasien",
//                    R.drawable.shape_menu_profile
//                ),
//                MenuKonsultasi(
//                    "profil_dokter",
//                    "Profil Dokter",
//                    R.drawable.shape_menu_profile_dokter
//                ),
//                MenuKonsultasi(
//                    "jadwal_dokter",
//                    "Jadwal Dokter",
//                    R.drawable.shape_menu_jadwal_dokter
//                ),
                MenuKonsultasi(
                    "cek_antrian",
                    "Cek Antrian",
                    R.drawable.cek_antrian_icon
                ),
//                MenuKonsultasi(
//                    "rekam_medis",
//                    "Rekam Medis",
//                    R.drawable.rekam_medis_icon
//                ),
                MenuKonsultasi(
                    "resep_dokter",
                    "Resep Dokter",
                    R.drawable.resep_dokter_icon
                )
//                MenuKonsultasi(
//                    "history_pembayaran",
//                    "Riwayat Pembayaran",
//                    R.drawable.riwayat_pembayaran_icon
//                ),
//                MenuKonsultasi(
//                    "logout",
//                    "Keluar Akun",
//                    R.drawable.ic_logout
//                )
            )
        }
    }

    val menuDoctorConsultation: MutableLiveData<List<MenuKonsultasi>> by lazy {
        MutableLiveData<List<MenuKonsultasi>>().apply {
            value = listOf(
                MenuKonsultasi(
                    "daftar",
                    "Profil Dokter",
                    R.drawable.profil_dokter_icon
                ),
                MenuKonsultasi(
                    "jadwal_terdaftar",
                    "Cek Jadwal",
                    R.drawable.jadwal_telekonsultasi_icon
                ),
                MenuKonsultasi(
                    "data_konsultasi",
                    "Data Konsultasi",
                    R.drawable.status_pendaftaran_icon
                ),
                MenuKonsultasi(
                    "assesment_pasien",
                    "Assesment Pasien",
                    R.drawable.isi_assessment_icon
                ),
//                MenuKonsultasi(
//                    "rekam_medis",
//                    "Rekam Medis",
//                    R.drawable.rekam_medis_icon_2
//                ),
                MenuKonsultasi(
                    "histori_log",
                    "Histori Log",
                    R.drawable.history_log_icon
                ),
                MenuKonsultasi(
                    "logout",
                    "Keluar Akun",
                    R.drawable.logout_icon
                )
            )
        }
    }

    val newsList: MutableLiveData<List<News>> by lazy {
        MutableLiveData<List<News>>()
    }

    val notificationList: MutableLiveData<List<Notification>> by lazy {
        MutableLiveData<List<Notification>>()
    }

    val notificationListSize: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
           value= "0"
        }
    }
    private fun getNews() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getNews(
                onSuccess = {
                    setLoading(false)
                    newsList.postValue(it)
                    Timber.d("List News = $it")
                },
                onError = {
                    setLoading(false)
                    newsList.postValue(listOf())
                }
            )
        }
    }

    fun getNotificationList() {
        setLoading(true)
        val param = hashMapOf<String?,String?>(
            "id_user" to user.value?.id,
            "status" to "all"
        )
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getNotification(
                param,
                onSuccess = {
                    setLoading(false)
                    notificationList.postValue(it)
                },
                onError = {
                    setLoading(false)
                    notificationList.postValue(listOf())
                }
            )
        }
    }

    fun getNotificationListUnread() {
        setLoading(true)
        val param = hashMapOf<String?,String?>(
            "id_user" to user.value?.id,
            "status" to "0"
        )
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getNotification(
                param,
                onSuccess = {
                    setLoading(false)
                    it?.let {
                        Timber.d("notificationSize = ${it.size}")
                        if(it.size < 100) {
                            notificationListSize.postValue(it.size.toString())
                        } else {
                            notificationListSize.postValue("99+")
                        }
                    } ?: run {
                        notificationListSize.postValue("0")
                    }
                },
                onError = {
                    Timber.d("notificationSize = 0")
                    setLoading(false)
                    notificationListSize.postValue("0")
                }
            )
        }
    }

    fun clearSharedPreference(){
        sharedPreference.setLoggedInStatus(false)
    }

    fun fetchUser(){
        user.postValue(sharedPreference.getUserValue())
        Timber.d("$TAG User: ${user.value}")
    }

    fun saveTokenNotification() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                token?.let {
                    Timber.d("fmcToken is $it")
                    uploadToken(it)
                }
            })
    }

    private fun uploadToken(token: String) {
        try {
            Timber.d(token)
            val lg = AppVar.BASE_URL
            AndroidNetworking.post(lg + "User/updateToken")
                .addBodyParameter("token", token)
                .addBodyParameter("id_user",  sharedPreference.getUserValue()?.id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Timber.d("response = $response")
                    }

                    override fun onError(anError: ANError?) {
                        anError?.printStackTrace()
                        Timber.d("error = $anError")
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
