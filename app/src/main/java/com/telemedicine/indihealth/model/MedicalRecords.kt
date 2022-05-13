package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class MedicalRecords(
    val id_jadwal_konsultasi: String? = "",
    val id_pasien: String? = "",
    val id_dokter: String? = "",
    val keluhan: String? = "",
    val tanggal_konsultasi:String?="",
    val nama_pasien: String = "",
    val diagnosis: String? = "",
    val nama_dokter: String = "",
    val poli: String? = "",
    val created_at: String? = "",
    val dirilis:String?,
    val order_status:String?="",
    val diverifikasi:String?,
    val no_medrec: String? = "",
    val hari: String? = "",
    val tanggal: String? = "",
    val jam: String? = ""
) : Parcelable {
    val getNameDoctor: String
        get() = nama_dokter.capitalize(Locale("in", "ID"))
    val getNamePatient: String
        get() = nama_pasien.capitalize(Locale("in", "ID"))
    val getTanggalKonsultasi: String
        get() = tanggal_konsultasi + ", " + jam


}