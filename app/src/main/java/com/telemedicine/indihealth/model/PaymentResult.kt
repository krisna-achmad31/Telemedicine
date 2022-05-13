package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Surya Mahadi on 11/28/2021
 */
@Parcelize
data class PaymentResult(
    val msisdn: String = "",
    val amount: String = "",
    val uniqueTransactionCode: String = "",
    val actionType: String = "",
    val actionData: String = "",
    val expiryTime: String = "",
    val expiredDateTime: String = ""
) : Parcelable {
    val getActionType: String
        get() = actionType
    val getTransactionCode: String
        get() = uniqueTransactionCode
    val getActionData: String
        get() = actionData
    val getExpire: String
        get() = expiryTime
}
