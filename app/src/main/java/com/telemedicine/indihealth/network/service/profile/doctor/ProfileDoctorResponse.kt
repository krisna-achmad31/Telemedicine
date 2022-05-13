package com.telemedicine.indihealth.network.service.profile.doctor

import com.telemedicine.indihealth.model.ProfileDoctor

data class ProfileDoctorResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:List<ProfileDoctor>
)