package com.telemedicine.indihealth.network.service.consultation.asessment

import com.telemedicine.indihealth.model.Assessment

data class AssessmentListResponse (
    val status: Boolean,
    val msg: String,
    val data: List<Assessment>
    )