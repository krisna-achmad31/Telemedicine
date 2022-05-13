package com.telemedicine.indihealth.network.service.consultation.payment

import com.telemedicine.indihealth.model.ListPaymetnMethod

data class PaymentListResponse (
    val status: Boolean? = false,
    val msg: String? = "",
    val data: List<ListPaymetnMethod>?,
)