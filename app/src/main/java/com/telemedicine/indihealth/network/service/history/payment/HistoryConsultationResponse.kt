package com.telemedicine.indihealth.network.service.history.payment

import com.telemedicine.indihealth.model.HistoryConsultation

data class HistoryConsultationResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:List<HistoryConsultation>
)