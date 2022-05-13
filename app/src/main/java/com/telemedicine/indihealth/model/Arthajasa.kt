package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Surya Mahadi on 11/28/2021
 */
@Parcelize
data class Arthajasa (
    val status: Boolean = false,
    val message: String = "",
    val data: PaymentResult,
) : Parcelable {
    val getStatus: Boolean
        get() = status

    val getMessage: String
        get() = message

    val getData: PaymentResult
        get() = data
}