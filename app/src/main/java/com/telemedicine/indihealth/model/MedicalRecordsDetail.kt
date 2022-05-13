package com.telemedicine.indihealth.model

import java.util.*

data class MedicalRecordsDetail(
    val diagnosis: String? = "",
    val id_registrasi: String? = "",
    val created_at: String? = "",
    val keluhan: String? = "",
    val tanggal_konsultasi:String?="",
    val nama_pasien: String = "",
    val tanggal_lahir_pasien: String? = "",
    val tempat_lahir_pasien: String? = "",
    val jk_pasien: String? = "",
    val nama_dokter: String = "",
    val dirilis:String?,
    val diverifikasi:String?,
    val id_dokter: String? = "",
    val poli: String? = "",
    val order_status:String?="",
    val no_medrec: String? = "",
    val foto_pasien: String? = "",
    val foto_dokter: String? = "",
    val list_obat: List<Drug> = mutableListOf(),
    var planning: String? = "",
    var kesimpulan: String? = "",
    var pemeriksaan: String? = "",
    var laboratorium: Int? = null,
    var radiologi: Int? = null
) {
    val getNameDoctor: String
        get() = nama_dokter?.capitalize(Locale("in", "ID"))
    val getNamePatient: String
        get() = nama_pasien.capitalize(Locale("in", "ID"))
    val getBirth: String
        get() = tempat_lahir_pasien + "/" + tanggal_lahir_pasien

    val getOrderStatus: String
        get(){
            return when(order_status){
                "1" -> "DELIVERED"
                else -> "PENDING"
            }
        }

    fun getPemeriksaanPlain(): String {
        var type = mutableListOf<String>()
        if (laboratorium != null && laboratorium == 1) {
            type.add("Laboratorium")
        }
        if (radiologi != null && radiologi == 1) {
            type.add("Radiologi")
        }
        var suffix = if (type.isNotEmpty()) {
            "(" + type.joinToString(separator = ", ") + ")"
        } else {
            ""
        }
        return "$pemeriksaan $suffix"
    }
}