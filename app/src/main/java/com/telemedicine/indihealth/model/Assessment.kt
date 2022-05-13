/* 
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */
package com.telemedicine.indihealth.model

import com.telemedicine.indihealth.R
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


data class Assessment(
    var id: String? = "",
    var id_pasien: String? = "",
    var id_dokter: String? = "",
    var id_jadwal_konsultasi: String? = "",
    var berat_badan: String? = "",
    var tinggi_badan: String? = "",
    var tekanan_darah: String? = "",
    var suhu: String? = "",
    var merokok: String? = "",
    var alkohol: String? = "",
    var kecelakaan: String? = "",
    var operasi: String? = "",
    var dirawat: String? = "",
    var keluhan: String? = "",
    var created_at: String? = "",
    var updated_at: String? = "",
    var updated_by: String? = "",
    var nama_dokter: String? = "",
    var poli: String? = "",
    var jam: String? = "",
    var foto: String? = "",
    var tanggal_konsultasi: String? = "",
    var riwayat_penyakit: String? = "",
    var riwayat_alergi: String? = "",
    var file_pemeriksaan_luar: List<AdditionFile> = listOf()
) {
    var getSmoke: Int
        get() {
            return when (merokok) {
                "1" -> R.id.consultation_assessment_radio_1
                "0" -> R.id.consultation_assessment_radio_1_0
                else -> 0
            }
        }
        set(value) {
            merokok = value.toString()
        }
    var getAlcohol: Int
        get() {
            return when (alkohol) {
                "1" -> R.id.consultation_assessment_radio_2
                "0" -> R.id.consultation_assessment_radio_2_0
                else -> 0
            }
        }
        set(value) {
            alkohol = value.toString()
        }
    var getAccident: Int
        get() {
            return when (kecelakaan) {
                "1" -> R.id.consultation_assessment_radio_3
                "0" -> R.id.consultation_assessment_radio_3_0
                else -> 0
            }
        }
        set(value) {
            kecelakaan = value.toString()
        }
    var getTreated: Int
        get() {
            return when (dirawat) {
                "1" -> R.id.consultation_assessment_radio_4
                "0" -> R.id.consultation_assessment_radio_4_0
                else -> 0
            }
        }
        set(value) {
            dirawat = value.toString()
        }
    var getOperated: Int
        get() {
            return when (operasi) {
                "1" -> R.id.consultation_assessment_radio_5
                "0" -> R.id.consultation_assessment_radio_5_0
                else -> 0
            }
        }
        set(value) {
            operasi = value.toString()
        }

    val getConsultationDate: String
        get() {
            var dateText = tanggal_konsultasi
            Timber.d("Tanggal Konsultasi = $dateText")
            try {
                val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                val inputDateStr = tanggal_konsultasi
                val date: Date? = inputFormat.parse(inputDateStr)
                dateText = outputFormat.format(date)
                Timber.d("Tanggal Konsultasi = $dateText")
            } catch (e: Exception) {

            }
            return "$dateText $jam"
        }
}

data class AdditionFile(
    val id: String,
    val file: String
)