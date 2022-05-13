package com.telemedicine.indihealth.network.service.consultation.registration

import com.telemedicine.indihealth.model.ConsultationDoctor


data class ConsultationDoctorResponse(
    val status:Boolean=false,
    val message:String="",
    val data:List<ConsultationDoctor>
)