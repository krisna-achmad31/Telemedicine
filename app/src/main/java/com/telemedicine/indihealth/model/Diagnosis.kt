package com.telemedicine.indihealth.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Diagnosis(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("aktif")
	val aktif: String? = null
): Parcelable
