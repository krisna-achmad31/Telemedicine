package com.telemedicine.indihealth.model

import java.text.NumberFormat
import java.util.*

data class RecipeDetail(
    val id: String? = "",
    val id_jadwal_konsultasi: String? = "",
    val id_pasien: String? = "",
    val id_dokter: String? = "",
    val id_obat: String? = "",
    val jumlah_obat: String? = "",
    val keterangan: String? = "",
    val dibatalkan: String? = "",
    val created_at: String? = "",
    val updated_at: String? = "",
    val id_kategori_obat: String? = "",
    val name: String? = "",
    val unit: String? = "",
    val harga_per_n_unit: Int = 0,
    val harga: Int = 0,
    val active: String? = "",
    val created_by: String? = "",
    val updated_by: String? = ""
) {
    val getStatusDibatalkan: String
        get() {
            return when (dibatalkan) {
                "0" -> "Tidak Dibatalkan"
                else -> "Dibatalkan"
            }
        }
    val getPrice: String
        get() {
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            val hasil1 = format.format(harga)
            val hasil2 = format.format(harga_per_n_unit)
            return (hasil1 + " / " + hasil2)
        }
}