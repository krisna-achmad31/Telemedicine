package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class News(
    val id:String="",
    val judul:String="",
    val foto:String="",
    val berita:String="",
    val created_at:String=""
) : Parcelable {
    val getTanggal: String
        get() {
            var dateText = created_at
            try {
                val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                val inputDateStr = created_at
                val date: Date? = inputFormat.parse(inputDateStr)
                dateText = outputFormat.format(date)
                Timber.d("fakhrii" + dateText)
            }catch (e : Exception){
                Timber.d("fakhrii" + e.message!!)
            }
            return dateText
        }
}