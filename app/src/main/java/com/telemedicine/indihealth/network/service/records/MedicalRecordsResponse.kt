package com.telemedicine.indihealth.network.service.records

import com.telemedicine.indihealth.model.MedicalRecords

data class MedicalRecordsResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:List<MedicalRecords>
)