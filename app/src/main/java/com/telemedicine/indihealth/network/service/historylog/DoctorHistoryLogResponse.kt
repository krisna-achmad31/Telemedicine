package com.telemedicine.indihealth.network.service.historylog

import com.telemedicine.indihealth.model.DoctorHistoryLog

data class DoctorHistoryLogResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:List<DoctorHistoryLog>
)