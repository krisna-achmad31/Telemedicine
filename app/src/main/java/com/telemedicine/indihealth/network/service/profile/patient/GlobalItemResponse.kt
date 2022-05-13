package com.telemedicine.indihealth.network.service.profile.patient

import com.telemedicine.indihealth.model.GlobalItem

data class GlobalItemResponse(
    val status: Boolean? = false,
    val msg: String? = "",
    val data: List<GlobalItem>? = null
)