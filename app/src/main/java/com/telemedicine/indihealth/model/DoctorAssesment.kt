package com.telemedicine.indihealth.model

import com.telemedicine.indihealth.R
import java.util.*


data class DoctorAssesment (

	var id : String? = "",
	var id_pasien : String? = "",
	var id_dokter : String? = "",
	var id_jadwal_konsultasi : String? = "",
	var berat_badan : String? = "",
	var tinggi_badan : String? = "",
	var tekanan_darah : String? = "",
	var suhu : String? = "",
	var merokok : String? = "",
	var alkohol : String? = "",
	var kecelakaan : String? = "",
	var operasi : String? = "",
	var dirawat : String? = "",
	var keluhan : String? = "",
	var created_at : String? = "",
	var updated_at : String? = "",
	var updated_by : String? = "",
	var nama_pasien : String = "",
	var foto : String? = ""
){
    val getName: String
        get() = nama_pasien.capitalize(Locale("in", "ID"))
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
}