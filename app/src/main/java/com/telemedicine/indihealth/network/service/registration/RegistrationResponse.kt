package com.telemedicine.indihealth.network.service.registration

import com.telemedicine.indihealth.model.User

data class RegistrationResponse(
    val status:Boolean? =false,
    val message:String?="",
    val data:User?
)