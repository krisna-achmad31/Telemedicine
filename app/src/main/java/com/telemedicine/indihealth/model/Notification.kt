package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Notification(
    val id: String = "",
    val id_user: String = "",
    val notifikasi: String = "",
    val direct_link: String = "",
    val tanggal: String = "",
    val status: String = ""
) : Parcelable {
    val getTanggal: String
        get() {
            var dateText = tanggal
            try {
                val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                val dateOutput: DateFormat = SimpleDateFormat("HH:mm, dd-MM-yyyy", Locale.US)
                //val hourOutput: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
                val inputDateStr = tanggal
                val date: Date? = inputFormat.parse(inputDateStr)
                dateText = dateOutput.format(date)
                //val hour: Date? = hourOutput.parse(inputDateStr)
                //val hourString = hourOutput.format(hour)
            } catch (e: Exception) {
                Timber.d("Error parsing date : ${e.message} ")
            }
            return dateText
        }
}