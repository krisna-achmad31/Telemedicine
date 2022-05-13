package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Surya Mahadi on 11/28/2021
 */
@Parcelize
data class ShipmentAddress(
    val alamatProvinsi: String = "",
    val alamatKota: String = "",
    val alamatKecamatan: String = "",
    val alamatKelurahan: String = "",
    val detailAlamat: String = "",
    val kodepos: String = "",
) : Parcelable {
}
