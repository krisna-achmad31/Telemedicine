package com.telemedicine.indihealth.network.service.doctor.consultation

import com.telemedicine.indihealth.model.AdditionalInfo

/**
 * Created by Surya Mahadi on 11/28/2021
 */
data class AdditionalInfoResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data: AdditionalInfo?
)