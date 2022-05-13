package com.telemedicine.indihealth.network.service.doctor.consultation

import com.telemedicine.indihealth.model.ScheduleDoctorConsultation

data class ScheduleDoctorConsultationResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data: List<ScheduleDoctorConsultation>?
)