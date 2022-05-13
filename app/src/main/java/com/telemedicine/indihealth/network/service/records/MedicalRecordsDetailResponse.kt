package com.telemedicine.indihealth.network.service.records

import com.telemedicine.indihealth.model.MedicalRecordsDetail

data class MedicalRecordsDetailResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:MedicalRecordsDetail
)