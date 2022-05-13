package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.NumberFormat
import java.util.*

@Parcelize
data class Recipe(
    val id: String? = "",
    val id_jadwal_konsultasi: String? = "",
    val id_pasien: String? = "",
    val id_dokter: String? = "",
    val id_obat: String? = "",
    val jumlah_obat: List<String?> = listOf(),
    val keterangan: String? = "",
    val dibatalkan: String? = "",
    val created_at: String? = "",
    val updated_at: String? = "",
    val nama_dokter: String = "",
    val poli: String? = "",
    val str_dokter: String? = "",
    val foto_dokter: String? = "",
    val nama_obat: String? = "",
    val bukti_pembayaran: String? = "",
    val detail_obat: List<String?> = listOf(),
    val harga_obat: List<String?> = listOf(),
    val harga_obat_per_n_unit: List<String?> = listOf(),
    val id_registrasi: String? = "",
    val tanggal_konsultasi: String? = "",
    val total_harga: Int = 0,
    val dirilis: String? = "",
    val diverifikasi: String? = "",
    val biaya_pengiriman: String? = "",
    val status_pembayaran_obat: String? = "",
    val harga: String? = "",
) : Parcelable {
    val getNameDoctor: String
        get() = nama_dokter.capitalize(Locale("in", "ID"))
    val getStatusDibatalkan: String
        get() {
            return when (dibatalkan) {
                "0" -> "Tidak Dibatalkan"
                else -> "Dibatalkan"
            }
        }
    val getStatusPembayaran: String
        get() {
            if(dibatalkan == "1") return "4"
            return when (status_pembayaran_obat) {
                "0" -> "0"
                "1" -> "1"
                "2" -> "2"
                else -> "3"
            }
        }

    val getHargaObat: String
        get() {
            var biayaKirim = 0
            biaya_pengiriman?.let { biayaKirim = it.toInt() }
            val hargaObat = total_harga - biayaKirim
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            val hasil = format.format(hargaObat)
            return (hasil)
        }

    val getBiayaPengiriman: String
        get() {
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            val hasil = format.format(biaya_pengiriman?.toInt())
            return (hasil)
        }
    val getTotalPrice: String
        get() {
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            val hasil = format.format(total_harga)
            return (hasil)
        }

    val getWaktu: String
        get() {
            val hasil = "Waktu Konsultasi: $tanggal_konsultasi"
            return (hasil)
        }

    val getDoctorSTR: String
        get() {
            return "No. STR: $str_dokter"
        }

    val getStatus: String
        get() {
            return when (status_pembayaran_obat) {
                "0" -> "Proses Verifikasi"
                "1" -> "Lunas"
                else -> "3"
            }
        }
}