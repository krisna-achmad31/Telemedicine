package com.telemedicine.indihealth.network.service.consultation.payment

data class OwlexaGetTocResponse(
    val data: List<String>? = listOf(),
    val status: String?="",
    val code: Int=0,
    val msg:String="",
    val transactionId: String=""
)