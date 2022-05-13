package com.telemedicine.indihealth.model

import java.util.*

data class Queue(
    val id: String? = "",
    val id_dokter: String? = "",
    val id_pasien: String? = "",
    val id_jadwal_konsultasi: String? = "",
    val tanggal_konsultasi:String?="",
    val antrian: String? = "",
    val status: String? = "",
    val created_at: String? = "",
    val nama_dokter: String = "",
    val poli: String? = "",
    val id_registrasi: String? = "",
    val hari: String? = "",
    val tanggal: String? = "",
    val jam: String? = "",
    val foto_dokter: String? = ""
) {
    val getNameDoctor: String
        get() = nama_dokter.capitalize(Locale("in", "ID"))
    val getStatus: String
        get() {
            return when (status) {
                "0" -> "Belum Konsultasi"
                else -> "Sedang Konsultasi"
            }
        }
    val getTanggalKonsultasi: String
        get() = tanggal + " " + jam
}