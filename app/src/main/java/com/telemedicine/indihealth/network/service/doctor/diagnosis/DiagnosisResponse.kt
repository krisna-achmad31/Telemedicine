package com.telemedicine.indihealth.network.service.doctor.diagnosis

import com.telemedicine.indihealth.model.Diagnosis

data class DiagnosisResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data: List<Diagnosis>?
)