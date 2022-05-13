package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Prescription(
    var obat : Medicine? = null,
    var jumlahObat : String? = "",
    var keterangan : String? = "",
    var unit: String? = "-"
) : Parcelable