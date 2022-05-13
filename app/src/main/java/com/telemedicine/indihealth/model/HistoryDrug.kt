package com.telemedicine.indihealth.model

import timber.log.Timber
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class HistoryDrug(
    val id: String? = "",
    val id_bukti: String? = "",
    val id_jadwal_konsultasi: String? = "",
    val created_at: String? = "",
    val foto_bukti: String? = "",
    val status_bukti: String? = "",
    val nama_dokter: String = "",
    val nama_pasien: String = "",
    val detail_obat: List<String?> = listOf(),
    var list_obat: String = "",
    val harga_obat: List<String?> = listOf(),
    val harga_obat_per_n_unit: List<String?> = listOf(),
    val total_harga: Int = 0,
    val tanggal_konsultasi: String? = "",
    val tanggal_upload: String? = "",
    val poli: String? = "",
    val jumlah_obat: List<String?> = listOf(),
    val claim_number: String? =""
) {
    val getStatusPembayaran: String
        get() {
            return when (status_bukti) {
                "0" -> "Sedang Diproses"
                "1" -> "Lunas"
                "2" -> "Ditolak"
                else -> "Belum Dibayar"
            }
        }
    val getStatusBukti: String
        get() {
            return when (status_bukti) {
                "0" -> "0"
                "1" -> "1"
                "2" -> "2"
                else -> "3"
            }
        }
    val getNameDoctor: String
        get() = nama_dokter.capitalize(Locale("in", "ID"))
    val getNamePatient: String
        get() = nama_pasien.capitalize(Locale("in", "ID"))
    val getPrice: String
        get() {
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            val hasil = format.format(total_harga)
            return (hasil)
        }

    val getTanggalKonsultasi: String?
        get() {
            var dateText = tanggal_konsultasi
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
                val inputDateStr = tanggal_konsultasi!!
                val date: Date? = inputFormat.parse(inputDateStr)
                dateText = outputFormat.format(date)
                Timber.d("fakhrii %s", dateText)
            }catch (e : Exception){
                Timber.d("fakhrii %s", e.message!!)
            }
            return dateText
        }

    val getTanggalUpload: String?
        get() {
            var dateText = tanggal_upload
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
                val inputDateStr = tanggal_upload!!
                val date: Date? = inputFormat.parse(inputDateStr)
                dateText = outputFormat.format(date)
                Timber.d("fakhrii %s", dateText)
            }catch (e : Exception){
                Timber.d("fakhrii %s", e.message!!)
            }
            return dateText
        }

    val getPaymentText: String
        get() {
            return if (claim_number.isNullOrEmpty()) "Transfer"
            else "Owlexa"
        }

    val getPayment: String
        get() {
            return if (claim_number.isNullOrEmpty()) "1"
            else "2"
        }
}