package com.telemedicine.indihealth.network.service.profile.patient

import com.telemedicine.indihealth.model.User

data class ProfilePatientResponse(
    val status:Boolean? =false,
    val message:String?="",
    val data:User?
)