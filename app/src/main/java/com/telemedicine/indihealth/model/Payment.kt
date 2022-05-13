package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.NumberFormat
import java.util.*

@Parcelize
data class Payment(
    val id: String? = "",
    val keterangan: String? = "",
    val id_status_pembayaran: String? = "",
    val id_fasyankes: String? = "",
    val id_jadwal: String? = "",
    val created_at: String? = "",
    val id_pasien: String? = "",
    val id_dokter: String? = "",
    val poli: String? = "",
    val hari: String? = "",
    val tanggal: String? = "",
    val waktu: String? = "",
    val harga: String? = "",
    val nama_dokter: String? = "",
    val foto: String? = "",
    val str_dokter: String? = "",
    val bukti_pembayaran: String? = "",
    val biaya_konsultasi: String?="",
    val biaya_administrasi: String="",
    val status:String="",
    val total_pembayaran:String="",
    val nama_lengkap:String="",
    val tanggal_lahir:String="",
    val card_number:String=""


) : Parcelable {
    val getTime: String
    get() {
        return "$hari. $waktu"
    }
    val getStatus: String
        get() {
            return when (id_status_pembayaran) {
                "0" -> "Belum Bayar"
                "1" -> "Lunas"
                else -> "Sedang Diproses"
            }
        }
    val getPrice: String
        get() {
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            return format.format(harga?.toInt())
        }


    val getBiayaKonsultasi: String?
        get() {
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            return format.format(biaya_konsultasi?.toInt())
        }
    val getBiayaAdministrasi: String
        get() {
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            return format.format(biaya_administrasi.toInt())
        }
    val getTotalPembayaran: String
        get() {
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            return format.format(total_pembayaran.toInt())
        }

    val getDoctorSTR: String
        get() {
            return "No. STR: $str_dokter"
        }
}