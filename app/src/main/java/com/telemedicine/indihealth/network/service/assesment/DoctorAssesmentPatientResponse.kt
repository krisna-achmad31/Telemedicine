package com.telemedicine.indihealth.network.service.assesment

import com.telemedicine.indihealth.model.DoctorAssesment

data class DoctorAssesmentPatientResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:List<DoctorAssesment>
)