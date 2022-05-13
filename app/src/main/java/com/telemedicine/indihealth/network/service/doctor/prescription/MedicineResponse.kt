package com.telemedicine.indihealth.network.service.doctor.prescription

import com.telemedicine.indihealth.model.Medicine

data class MedicineResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data: List<Medicine>?
)