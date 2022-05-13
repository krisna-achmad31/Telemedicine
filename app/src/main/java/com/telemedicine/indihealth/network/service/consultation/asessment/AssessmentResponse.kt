package com.telemedicine.indihealth.network.service.consultation.asessment

import com.telemedicine.indihealth.model.Assessment

data class AssessmentResponse(
    val status: Boolean? = false,
    val msg: String? = "",
    val data: Assessment?
)