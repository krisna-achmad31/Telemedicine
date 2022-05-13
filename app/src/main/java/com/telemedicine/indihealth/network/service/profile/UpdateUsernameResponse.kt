package com.telemedicine.indihealth.network.service.profile

import com.telemedicine.indihealth.model.User

data class UpdateUsernameResponse(
    val status:Boolean? =false,
    val message:String?="",
    val data: User?
)