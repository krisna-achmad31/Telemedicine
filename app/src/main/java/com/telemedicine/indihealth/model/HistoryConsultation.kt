package com.telemedicine.indihealth.model

import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class HistoryConsultation(
    val id: String? = "",
    val id_pasien: String? = "",
    val tanggal_konsultasi: String? = "",
    val id_dokter: String? = "",
    val id_registrasi: String? = "",
    val photo: String? = "",
    val status: String? = "",
    val created_at: String? = "",
    val updated_at: String? = "",
    val nama_dokter: String = "",
    val poli: String = "",
    val biaya_administrasi: String = "",
    val id_jadwal: String? = "",
    val foto_dokter: String? = "",
    val metode_pembayaran: String? = ""
) {
    val getStatusPembayaran: String
        get() {
            return when (status) {
                "0" -> "Sedang Diproses"
                "1" -> "Lunas"
                "2" -> "Ditolak"
                else -> "Belum Dibayar"
            }
        }
    val getStatusBukti: String
        get() {
            return when (status) {
                "0" -> "0"
                "1" -> "1"
                "2" -> "2"
                else -> "3"
            }
        }
    val getName: String
        get() = nama_dokter.capitalize(Locale("in", "ID"))

    val getTanggalKonsultasi: String?
        get() {
            var dateText = tanggal_konsultasi
            try {
                val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
                val dateOutput: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US)
                //val hourOutput: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
                val inputDateStr = tanggal_konsultasi
                val date: Date? = inputFormat.parse(inputDateStr)
                dateText = "Waktu Konsultasi: ${dateOutput.format(date)}"
                //val hour: Date? = hourOutput.parse(inputDateStr)
                //val hourString = hourOutput.format(hour)
            } catch (e: Exception) {
                Timber.d("Error parsing date : ${e.message} ")
            }
            return dateText
        }

    val getBiaya: String
        get() {
            return "Biaya: $biaya_administrasi"
        }


    val getPayment: String
        get() {
            return when (status) {
                "1" -> "Metode Pembayaran: Transfer"
                "2" -> "Metode Pembayaran: Owlexa"
                else -> "Metode Pembayaran: Transfer"
            }
        }
}