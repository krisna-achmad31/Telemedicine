package com.telemedicine.indihealth.network.service.consultation.payment

import com.telemedicine.indihealth.model.Payment

data class PaymentResponse(
    val status: Boolean? = false,
    val msg: String? = "",
    val data: List<Payment>?,
)