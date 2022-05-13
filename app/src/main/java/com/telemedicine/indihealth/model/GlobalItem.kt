package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GlobalItem(
    val id : String? = "",
    val id_provinsi: String? = "",
    val id_kota: String? = "",
    val id_kecamatan: String? = "",
    val name: String? = "",
    val namepj: String? = "",
    val aktif: String? = ""
) : Parcelable