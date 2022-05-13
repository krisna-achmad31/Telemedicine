package com.telemedicine.indihealth.network.service.main

import com.telemedicine.indihealth.model.Notification

data class NotificationResponse(
    val status:Boolean,
    val msg:String,
    val data:List<Notification>
)