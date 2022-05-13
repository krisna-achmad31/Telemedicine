package com.telemedicine.indihealth.ui.fragment.doctor.container

import androidx.lifecycle.MutableLiveData
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.model.MenuKonsultasi

class DoctorContainerViewModel: BaseViewModel() {
    val menuLainnya: MutableLiveData<List<MenuKonsultasi>> by lazy {
        MutableLiveData<List<MenuKonsultasi>>().apply {
            value = listOf(
                MenuKonsultasi(
                    "profil_dokter",
                    "Profil Dokter",
                    R.drawable.shape_menu_profile_dokter
                ),
                MenuKonsultasi(
                    "jadwal_dokter",
                    "Jadwal Dokter",
                    R.drawable.shape_menu_jadwal_dokter
                ),
            )
        }
    }
}
