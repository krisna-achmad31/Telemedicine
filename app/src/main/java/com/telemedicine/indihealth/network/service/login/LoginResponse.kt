package com.telemedicine.indihealth.network.service.login

import com.telemedicine.indihealth.model.User

data class LoginResponse(
    val status:Boolean? =false,
    val message:String?="",
    val data:User?
)