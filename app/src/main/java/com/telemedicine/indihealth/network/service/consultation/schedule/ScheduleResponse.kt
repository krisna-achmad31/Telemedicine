package com.telemedicine.indihealth.network.service.consultation.schedule

import com.telemedicine.indihealth.model.Schedule

data class ScheduleResponse(
    val status:Boolean?=false,
    val msg:String?="",
    val data:List<Schedule>?
)