package com.telemedicine.indihealth.network.service.schedule

import com.telemedicine.indihealth.model.DoctorSchedule

data class DoctorScheduleResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:List<DoctorSchedule>
)