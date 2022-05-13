package com.telemedicine.indihealth.network.service.profile.doctor

import com.telemedicine.indihealth.model.User

data class DoctorResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:User?
)