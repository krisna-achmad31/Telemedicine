package com.telemedicine.indihealth.network.service.history.profile

import com.telemedicine.indihealth.model.User

data class ProfileDoctorResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:User?
)