package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ListPaymetnMethod (
    val payment_id : String? = "",
    val logo : String = "",
    val payment : String = "",
    var selected : Boolean = false,
    var no_rek : String = ""
) : Parcelable {
    val getLogo: String
        get() = logo

    val getPayment: String
        get() = payment

    val getSelected: Boolean
        get() = selected
}