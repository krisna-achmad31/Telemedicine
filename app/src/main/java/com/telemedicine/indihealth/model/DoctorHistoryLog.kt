package com.telemedicine.indihealth.model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class DoctorHistoryLog (
	val id : String? = "",
	val activity : String? = "",
	val id_user : String? = "",
	val target_id_user : String? = "",
	val activity_at : String? = "",
	val nama_dokter : String = "",
	val nama_pasien : String = ""

) {
	val getNamePatient: String
        get() = nama_pasien.capitalize(Locale("in", "ID"))

	val getNameDoctor: String
        get() = nama_dokter.capitalize(Locale("in", "ID"))

	val getActivityAt: String?
		get() {
			var dateText = activity_at
			try {
				val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
				val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy, HH:mm:ss", Locale.US)
				val inputDateStr = activity_at
				val date: Date? = inputFormat.parse(inputDateStr)
				dateText = outputFormat.format(date)
			}catch (e : Exception){

			}
			return dateText
		}
}